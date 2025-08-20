package com.proyecto.serviceimpl;

import com.proyecto.dao.ProveedorDao;
import com.proyecto.domain.Proveedor;
import com.proyecto.service.FirebaseStorageService;
import com.proyecto.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorDao proveedorDao;
    private final FirebaseStorageService firebase;

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarTodos() {
        return proveedorDao.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscar(String filtroNombre) {
        if (filtroNombre == null || filtroNombre.isBlank()) {
            return proveedorDao.findByActivoTrue();
        }
        return proveedorDao.buscarActivos(filtroNombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor getById(Long idProveedor) {
        return proveedorDao.findById(idProveedor).orElse(null);
    }

    @Override
    @Transactional
    public Proveedor guardar(Proveedor proveedor, MultipartFile imagenFile) {
        proveedor = proveedorDao.save(proveedor);

        if (imagenFile != null && !imagenFile.isEmpty()) {
            String url = firebase.cargaImagen(imagenFile, "proveedores", proveedor.getIdProveedor());
            proveedor.setRutaImagen(url);
            proveedor = proveedorDao.save(proveedor);
        }
        return proveedor;
    }

    @Override
    @Transactional
    public void eliminar(Long idProveedor) {
        proveedorDao.deleteById(idProveedor);
    }
}
