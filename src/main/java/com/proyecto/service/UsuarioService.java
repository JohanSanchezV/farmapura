package com.proyecto.service;

import com.proyecto.domain.Usuario;
import com.proyecto.service.dto.RegistroDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UsuarioService {

    void registrarCliente(RegistroDto dto, MultipartFile imagenFile);

    List<Usuario> listarTodos();
    List<Usuario> buscar(String q);
    Usuario getById(Long idUsuario);

    Usuario guardar(Usuario usuario, MultipartFile imagenFile, String passwordPlano, Long idRol);

    boolean eliminar(Long idUsuario);

    void toggleActivo(Long idUsuario);
}
