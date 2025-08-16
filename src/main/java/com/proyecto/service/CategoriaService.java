package com.proyecto.service;

import com.proyecto.domain.Categoria;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CategoriaService {
    List<Categoria> getCategorias(boolean soloActivas);
    Categoria getById(Long idCategoria);
    Categoria guardar(Categoria categoria, MultipartFile imagen);
    void eliminar(Long idCategoria);
}

