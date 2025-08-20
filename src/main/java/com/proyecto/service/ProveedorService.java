package com.proyecto.service;

import com.proyecto.domain.Proveedor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProveedorService {
    List<Proveedor> listarTodos();
    List<Proveedor> buscar(String filtroNombre);
    Proveedor getById(Long idProveedor);
    Proveedor guardar(Proveedor proveedor, MultipartFile imagenFile);
    void eliminar(Long idProveedor);
}
