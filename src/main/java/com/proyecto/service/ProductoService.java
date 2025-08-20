package com.proyecto.service;

import com.proyecto.domain.Categoria;
import com.proyecto.domain.Producto;
import com.proyecto.service.dto.InventarioItem;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductoService {

    List<Producto> listarActivos();
    List<Producto> listarPorCategoria(Categoria categoria);
    List<Producto> buscar(String q);
    Producto getById(Long idProducto);

    Producto guardar(Producto producto, MultipartFile imagenFile);
    void eliminar(Long idProducto);

    /** Proxy opcional (delegado al ProductoDao). */
    List<InventarioItem> consultaInventario(String q);
}
