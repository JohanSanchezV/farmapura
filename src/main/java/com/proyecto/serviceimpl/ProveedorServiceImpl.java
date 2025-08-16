package com.proyecto.serviceimpl;

import com.proyecto.dao.ProveedorDao;
import com.proyecto.domain.Proveedor;
import com.proyecto.service.ProveedorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorDao proveedorDao;

    @Override @Transactional(readOnly = true)
    public List<Proveedor> listar() { return proveedorDao.findAll(); }

    @Override @Transactional(readOnly = true)
    public Proveedor getById(Long idProveedor) {
        return proveedorDao.findById(idProveedor).orElse(null);
    }

    @Override @Transactional
    public Proveedor guardar(Proveedor proveedor) { return proveedorDao.save(proveedor); }

    @Override @Transactional
    public void eliminar(Long idProveedor) { proveedorDao.deleteById(idProveedor); }
}
