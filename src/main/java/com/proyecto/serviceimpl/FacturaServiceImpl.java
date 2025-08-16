package com.proyecto.serviceimpl;

import com.proyecto.dao.FacturaDao;
import com.proyecto.dao.FacturaDetalleDao;
import com.proyecto.dao.ProductoDao;
import com.proyecto.dao.UsuarioDao;
import com.proyecto.domain.*;
import com.proyecto.service.FacturaService;
import com.proyecto.service.InventarioSalidaService;
import com.proyecto.service.dto.LineaVenta;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private static final BigDecimal IVA = new BigDecimal("0.13");

    private final FacturaDao facturaDao;
    private final FacturaDetalleDao detalleDao;
    private final UsuarioDao usuarioDao;
    private final ProductoDao productoDao;
    private final InventarioSalidaService inventarioSalidaService;

    @Override
    @Transactional
    public Factura checkout(Long idUsuario, List<LineaVenta> lineas) {
        var usuario = usuarioDao.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        BigDecimal subtotal = BigDecimal.ZERO;
        List<FacturaDetalle> detalles = new ArrayList<>();

        for (LineaVenta lv : lineas) {
            var producto = productoDao.findById(lv.idProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + lv.idProducto()));

            if (Boolean.FALSE.equals(producto.getActivo())) {
                throw new IllegalStateException("Producto inactivo: " + producto.getNombre());
            }

            Integer cantidad = lv.cantidad();
            if (cantidad == null || cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad inválida para " + producto.getNombre());
            }

            BigDecimal precio = producto.getPrecio();
            BigDecimal totalLinea = precio.multiply(BigDecimal.valueOf(cantidad));
            subtotal = subtotal.add(totalLinea);

            // construir detalle con setters
            var det = new FacturaDetalle();
            det.setProducto(producto);
            det.setCantidad(cantidad);
            det.setPrecioUnitario(precio);
            det.setTotalLinea(totalLinea);
            detalles.add(det);
        }

        BigDecimal descuento = BigDecimal.ZERO;
        BigDecimal imponible = subtotal.subtract(descuento);
        BigDecimal impuesto = imponible.multiply(IVA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = imponible.add(impuesto);

        var factura = new Factura();
        factura.setUsuario(usuario);
        factura.setSubtotal(subtotal);
        factura.setDescuento(descuento);
        factura.setImpuesto(impuesto);
        factura.setTotal(total);
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setFormaPago(FormaPago.EFECTIVO);
        factura = facturaDao.save(factura);

        // vincular factura en cada detalle y guardar
        for (var d : detalles) d.setFactura(factura);
        detalleDao.saveAll(detalles);

        // registrar salidas (trigger descuenta stock)
        for (var d : detalles) {
            var salida = new InventarioSalida();
            salida.setProducto(d.getProducto());
            salida.setCantidad(d.getCantidad());
            salida.setMotivo(MotivoSalida.VENTA);
            salida.setObservaciones("Descarga por factura " + factura.getIdFactura());
            inventarioSalidaService.registrar(salida);
        }

        factura.setDetalles(detalles);
        return factura;
    }
}
