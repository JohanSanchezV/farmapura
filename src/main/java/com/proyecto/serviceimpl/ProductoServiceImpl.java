package com.proyecto.serviceimpl;

import com.proyecto.dao.ProductoDao;
import com.proyecto.domain.Categoria;
import com.proyecto.domain.Producto;
import com.proyecto.service.FirebaseStorageService;
import com.proyecto.service.ProductoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDao productoDao;
    private final FirebaseStorageService firebase;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoDao.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(Categoria categoria) {
        return productoDao.findByCategoriaAndActivoTrue(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscar(String filtroNombre) {
        return productoDao.findByNombreContainingIgnoreCaseAndActivoTrue(filtroNombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto getById(Long idProducto) {
        return productoDao.findById(idProducto).orElse(null);
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto, MultipartFile imagen) {
        producto = productoDao.save(producto);

        if (imagen != null && !imagen.isEmpty()) {
            String url = firebase.cargaImagen(imagen, "productos", producto.getIdProducto());
            producto.setRutaImagen(url);
            producto = productoDao.save(producto);
        }
        return producto;
    }

    @Override
    @Transactional
    public void eliminar(Long idProducto) {
        productoDao.deleteById(idProducto);
    }
}
