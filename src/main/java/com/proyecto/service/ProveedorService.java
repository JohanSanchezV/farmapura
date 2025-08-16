package com.proyecto.service;

import com.proyecto.domain.Proveedor;
import java.util.List;

public interface ProveedorService {
    List<Proveedor> listar();
    Proveedor getById(Long idProveedor);
    Proveedor guardar(Proveedor proveedor);
    void eliminar(Long idProveedor);
}

