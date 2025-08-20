package com.proyecto.serviceimpl;

import com.proyecto.dao.CategoriaDao;
import com.proyecto.domain.Categoria;
import com.proyecto.service.CategoriaService;
import com.proyecto.service.FirebaseStorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaDao categoriaDao;
    private final FirebaseStorageService firebase;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean soloActivas) {
        var lista = categoriaDao.findAll();
        if (soloActivas) {
            // Si 'activo' es Boolean:
            lista.removeIf(c -> !Boolean.TRUE.equals(c.getActivo()));
        }
        return lista;
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria getById(Long idCategoria) {
        return categoriaDao.findById(idCategoria).orElse(null);
    }

    @Override
    @Transactional
    public Categoria guardar(Categoria categoria, MultipartFile imagen) {
        categoria = categoriaDao.save(categoria);

        if (imagen != null && !imagen.isEmpty()) {
            String url = firebase.cargaImagen(imagen, "categorias", categoria.getIdCategoria());
            categoria.setRutaImagen(url);
            categoria = categoriaDao.save(categoria);
        }
        return categoria;
    }

    @Override
    @Transactional
    public void eliminar(Long idCategoria) {
        categoriaDao.deleteById(idCategoria);
    }
}
