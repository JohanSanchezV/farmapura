package com.proyecto.service;

import com.proyecto.domain.Cliente;
import com.proyecto.domain.Usuario;

public interface RegistroService {
    Usuario registrarCliente(Usuario usuario, Cliente cliente); // asigna rol CLIENTE
}
