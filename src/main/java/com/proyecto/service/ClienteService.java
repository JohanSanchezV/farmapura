package com.proyecto.service;

import com.proyecto.domain.Cliente;
import com.proyecto.domain.Usuario;
import java.util.List;

public interface ClienteService {

    List<Cliente> listarTodos();
    List<Cliente> buscar(String q);
    Cliente getById(Long idCliente);

    Cliente guardar(Cliente cliente);  
    void eliminar(Long idCliente);

    List<Usuario> usuariosDisponibles();
    List<Usuario> usuariosDisponiblesIncluyendo(Usuario actual);
}
