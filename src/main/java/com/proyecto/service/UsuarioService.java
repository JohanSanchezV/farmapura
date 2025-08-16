package com.proyecto.service;

import com.proyecto.service.dto.RegistroDto;
import org.springframework.web.multipart.MultipartFile;

public interface UsuarioService {
    void registrarCliente(RegistroDto dto, MultipartFile imagenFile);
}