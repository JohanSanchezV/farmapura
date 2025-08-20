package com.proyecto.serviceimpl;

import com.proyecto.dao.FacturaDao;
import com.proyecto.dao.FacturaDetalleDao;
import com.proyecto.dao.ProductoDao;
import com.proyecto.dao.UsuarioDao;
import com.proyecto.domain.EstadoFactura;
import com.proyecto.domain.Factura;
import com.proyecto.domain.FacturaDetalle;
import com.proyecto.domain.FormaPago;
import com.proyecto.domain.InventarioSalida;
import com.proyecto.domain.MotivoSalida;
import com.proyecto.service.FacturaService;
import com.proyecto.service.InventarioService;
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
    private final InventarioService inventarioService;

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

            if (producto.getStock() != null && producto.getStock() < cantidad) {
                throw new IllegalStateException("Stock insuficiente para " + producto.getNombre());
            }

            BigDecimal precio = producto.getPrecio();
            BigDecimal totalLinea = precio.multiply(BigDecimal.valueOf(cantidad));
            subtotal = subtotal.add(totalLinea);

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
        BigDecimal total = imponible.add(impuesto).setScale(2, RoundingMode.HALF_UP);

        var factura = new Factura();
        factura.setUsuario(usuario);
        factura.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        factura.setDescuento(descuento.setScale(2, RoundingMode.HALF_UP));
        factura.setImpuesto(impuesto);
        factura.setTotal(total);
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setFormaPago(FormaPago.EFECTIVO);
        factura = facturaDao.save(factura);

        for (var d : detalles) d.setFactura(factura);
        detalleDao.saveAll(detalles);

        for (var d : detalles) {
            var salida = new InventarioSalida();
            salida.setProducto(d.getProducto());
            salida.setCantidad(d.getCantidad());
            salida.setMotivo(MotivoSalida.VENTA);
            salida.setObservaciones("Descarga por factura " + factura.getIdFactura());
            inventarioService.guardarSalida(salida);
        }

        factura.setDetalles(detalles);
        return factura;
    }
}
