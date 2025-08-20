package com.proyecto.serviceimpl;

import com.proyecto.dao.ProductoDao;
import com.proyecto.domain.Categoria;
import com.proyecto.domain.Producto;
import com.proyecto.service.FirebaseStorageService;
import com.proyecto.service.ProductoService;
import com.proyecto.service.dto.InventarioItem; // <-- IMPORT NECESARIO
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
        if (categoria == null || categoria.getIdCategoria() == null) {
            return listarActivos();
        }
        // Si tienes un método específico en el dao úsalo; si no, filtra en memoria.
        return productoDao.findByActivoTrue().stream()
                .filter(p -> p.getCategoria() != null
                          && p.getCategoria().getIdCategoria().equals(categoria.getIdCategoria()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscar(String q) {
        if (q == null || q.isBlank()) return listarActivos();
        // Si tienes un método findByNombreContainingIgnoreCase úsalo; si no, filtra en memoria.
        String term = q.trim().toLowerCase();
        return productoDao.findByActivoTrue().stream()
                .filter(p -> (p.getNombre() != null && p.getNombre().toLowerCase().contains(term)) ||
                             (p.getCategoria() != null && p.getCategoria().getNombre() != null &&
                              p.getCategoria().getNombre().toLowerCase().contains(term)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto getById(Long idProducto) {
        return productoDao.findById(idProducto).orElse(null);
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto, MultipartFile imagenFile) {
        // Alta o modificación
        var saved = productoDao.save(producto);

        // Imagen en Firebase (opcional)
        if (imagenFile != null && !imagenFile.isEmpty()) {
            String url = firebase.cargaImagen(imagenFile, "productos", saved.getIdProducto());
            saved.setRutaImagen(url);
            saved = productoDao.save(saved);
        }
        return saved;
    }

    @Override
    @Transactional
    public void eliminar(Long idProducto) {
        productoDao.deleteById(idProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioItem> consultaInventario(String q) {
        return productoDao.consultaInventario(q == null ? "" : q.trim());
    }
}
