package com.proyecto.service;

import com.proyecto.domain.Categoria;
import com.proyecto.domain.Producto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductoService {
    List<Producto> listarActivos();
    List<Producto> listarPorCategoria(Categoria categoria);
    List<Producto> buscar(String filtroNombre);
    Producto getById(Long idProducto);
    Producto guardar(Producto producto, MultipartFile imagen);
    void eliminar(Long idProducto);
}
