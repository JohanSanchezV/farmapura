package com.proyecto.serviceimpl;

import com.proyecto.dao.InventarioEntradaDao;
import com.proyecto.dao.InventarioSalidaDao;
import com.proyecto.dao.ProductoDao;
import com.proyecto.domain.InventarioEntrada;
import com.proyecto.domain.InventarioSalida;
import com.proyecto.domain.Producto;
import com.proyecto.service.InventarioService;
import com.proyecto.service.dto.InventarioItem;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioEntradaDao entradaDao;
    private final InventarioSalidaDao salidaDao;
    private final ProductoDao productoDao;

    // ---------- Consulta ----------
    @Override
    @Transactional(readOnly = true)
    public List<InventarioItem> consultaInventario(String q) {
        return productoDao.consultaInventario(q == null ? "" : q.trim());
    }

    // ---------- Entradas ----------
    @Override
    @Transactional(readOnly = true)
    public List<InventarioEntrada> listarEntradas(String q) {
        if (q == null || q.isBlank()) return entradaDao.findAll();
        return entradaDao.buscar(q.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioEntrada getEntrada(Long idEntrada) {
        return entradaDao.findById(idEntrada).orElse(null);
    }

    @Override
    @Transactional
    public InventarioEntrada guardarEntrada(InventarioEntrada e) {
        if (e == null || e.getProducto() == null || e.getProducto().getIdProducto() == null) {
            throw new IllegalArgumentException("Entrada inválida: producto requerido.");
        }
        if (e.getCantidad() == null || e.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad de entrada debe ser mayor a 0.");
        }

        // Fecha
        if (e.getFecha() == null) {
            e.setFecha(LocalDateTime.now());
        }

        // Guarda la entrada
        e = entradaDao.save(e);

        // Sube stock en producto
        Long idProd = e.getProducto().getIdProducto();
        Producto prod = productoDao.findById(idProd)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        int stockActual = (prod.getStock() == null) ? 0 : prod.getStock();
        prod.setStock(stockActual + e.getCantidad());
        productoDao.save(prod);

        return e;
    }

    @Override
    @Transactional
    public void eliminarEntrada(Long idEntrada) {
        var e = entradaDao.findById(idEntrada)
                .orElseThrow(() -> new IllegalArgumentException("Entrada no encontrada"));

        Long idProd = e.getProducto().getIdProducto();
        Producto prod = productoDao.findById(idProd)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        int stockActual = (prod.getStock() == null) ? 0 : prod.getStock();
        if (stockActual < e.getCantidad()) {
            throw new IllegalStateException("No se puede eliminar la entrada: dejaría stock negativo.");
        }
        prod.setStock(stockActual - e.getCantidad());
        productoDao.save(prod);

        entradaDao.deleteById(idEntrada);
    }

    // ---------- Salidas ----------
    @Override
    @Transactional(readOnly = true)
    public List<InventarioSalida> listarSalidas(String q) {
        if (q == null || q.isBlank()) return salidaDao.findAll();
        return salidaDao.buscar(q.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioSalida getSalida(Long idSalida) {
        return salidaDao.findById(idSalida).orElse(null);
    }

    @Override
    @Transactional
    public InventarioSalida guardarSalida(InventarioSalida s) {
        if (s == null || s.getProducto() == null || s.getProducto().getIdProducto() == null) {
            throw new IllegalArgumentException("Salida inválida: producto requerido.");
        }
        if (s.getCantidad() == null || s.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad de salida debe ser mayor a 0.");
        }

        // Fecha
        if (s.getFecha() == null) {
            s.setFecha(LocalDateTime.now());
        }

        // Guarda la salida
        s = salidaDao.save(s);

        // Baja stock en producto
        Long idProd = s.getProducto().getIdProducto();
        Producto prod = productoDao.findById(idProd)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        int stockActual = (prod.getStock() == null) ? 0 : prod.getStock();
        if (stockActual < s.getCantidad()) {
            throw new IllegalStateException("Stock insuficiente para realizar la salida.");
        }
        prod.setStock(stockActual - s.getCantidad());
        productoDao.save(prod);

        return s;
    }

    @Override
    @Transactional
    public void eliminarSalida(Long idSalida) {
        var s = salidaDao.findById(idSalida)
                .orElseThrow(() -> new IllegalArgumentException("Salida no encontrada"));

        Long idProd = s.getProducto().getIdProducto();
        Producto prod = productoDao.findById(idProd)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        int stockActual = (prod.getStock() == null) ? 0 : prod.getStock();
        prod.setStock(stockActual + s.getCantidad());
        productoDao.save(prod);

        salidaDao.deleteById(idSalida);
    }
}
