package com.proyecto.service;

import com.proyecto.domain.Proveedor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProveedorService {
    /** Todos los proveedores (activos e inactivos). */
    List<Proveedor> listarTodos();

    /** Solo proveedores activos (útil para inventario, etc.). */
    List<Proveedor> listarActivos();

    /** Búsqueda sobre activos (nombre/cedula). */
    List<Proveedor> buscar(String filtroNombre);

    Proveedor getById(Long idProveedor);

    Proveedor guardar(Proveedor proveedor, MultipartFile imagenFile);

    void eliminar(Long idProveedor);
}
