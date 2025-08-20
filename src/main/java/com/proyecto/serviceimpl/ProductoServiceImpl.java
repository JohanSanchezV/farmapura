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

        Producto target;

        if (producto.getIdProducto() != null) {
            target = productoDao.findById(producto.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + producto.getIdProducto()));

            target.setNombre(producto.getNombre());
            target.setDescripcion(producto.getDescripcion());
            target.setPrecio(producto.getPrecio());
            target.setStock(producto.getStock());
            target.setPuntoReorden(producto.getPuntoReorden());
            target.setFechaVencimiento(producto.getFechaVencimiento());
            target.setCategoria(producto.getCategoria());
            if (producto.getActivo() != null) {
                target.setActivo(producto.getActivo());
            }

            if ((imagen == null || imagen.isEmpty()) && producto.getRutaImagen() != null) {
                target.setRutaImagen(producto.getRutaImagen());
            }

        } else {
            target = producto;
            if (target.getActivo() == null) {
                target.setActivo(Boolean.TRUE);
            }
        }

        target = productoDao.save(target);

        if (imagen != null && !imagen.isEmpty()) {
            String url = firebase.cargaImagen(imagen, "productos", target.getIdProducto());
            target.setRutaImagen(url);
            target = productoDao.save(target);
        }

        return target;
    }

    @Override
    @Transactional
    public void eliminar(Long idProducto) {
        productoDao.deleteById(idProducto);
    }
}
