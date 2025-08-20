package com.proyecto.serviceimpl;

import com.proyecto.dao.InventarioEntradaDao;
import com.proyecto.dao.InventarioSalidaDao;
import com.proyecto.dao.ProductoDao;
import com.proyecto.domain.InventarioEntrada;
import com.proyecto.domain.InventarioSalida;
import com.proyecto.domain.Producto;
import com.proyecto.service.InventarioService;
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

    @Override
    @Transactional(readOnly = true)
    public List<Producto> consultaInventario(String q) {
        try {
            if (q == null || q.isBlank()) {
                try { return productoDao.getClass()
                        .getMethod("findByActivoTrue")
                        != null ? (List<Producto>) productoDao.findByActivoTrue()
                                : productoDao.findAll();
                } catch (NoSuchMethodException ignore) {
                    return productoDao.findAll();
                }
            } else {
                try {
                    return (List<Producto>) productoDao.getClass()
                            .getMethod("buscarActivos", String.class)
                            .invoke(productoDao, q.trim());
                } catch (Exception ignored) {
                    try {
                        return (List<Producto>) productoDao.getClass()
                                .getMethod("findByNombreContainingIgnoreCase", String.class)
                                .invoke(productoDao, q.trim());
                    } catch (Exception e2) {
                        return productoDao.findAll();
                    }
                }
            }
        } catch (Exception ex) {
            return productoDao.findAll();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioEntrada> listarEntradas(String q) {
        return (q == null || q.isBlank())
                ? entradaDao.findAllByOrderByFechaDesc()
                : entradaDao.buscar(q.trim());
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

        e.setFecha(LocalDateTime.now());
        e = entradaDao.save(e);

        var prod = productoDao.findById(e.getProducto().getIdProducto())
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

        var prod = productoDao.findById(e.getProducto().getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        int stockActual = (prod.getStock() == null) ? 0 : prod.getStock();
        if (stockActual < e.getCantidad()) {
            throw new IllegalStateException("No se puede eliminar la entrada: dejaría stock negativo.");
        }
        prod.setStock(stockActual - e.getCantidad());
        productoDao.save(prod);

        entradaDao.deleteById(idEntrada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioSalida> listarSalidas(String q) {
        return (q == null || q.isBlank())
                ? salidaDao.findAllByOrderByFechaDesc()
                : salidaDao.buscar(q.trim());
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

        s.setFecha(LocalDateTime.now());
        s = salidaDao.save(s);

        var prod = productoDao.findById(s.getProducto().getIdProducto())
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

        var prod = productoDao.findById(s.getProducto().getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        int stockActual = (prod.getStock() == null) ? 0 : prod.getStock();
        prod.setStock(stockActual + s.getCantidad());
        productoDao.save(prod);

        salidaDao.deleteById(idSalida);
    }
}
