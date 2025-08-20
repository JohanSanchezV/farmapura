package com.proyecto.serviceimpl;

import com.proyecto.dao.ClienteDao;
import com.proyecto.dao.UsuarioDao;
import com.proyecto.domain.Cliente;
import com.proyecto.domain.Usuario;
import com.proyecto.service.ClienteService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteDao clienteDao;
    private final UsuarioDao usuarioDao;

    @Override @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteDao.findAllConUsuario();
    }

    @Override @Transactional(readOnly = true)
    public List<Cliente> buscar(String q) {
        if (q == null || q.isBlank()) return listarTodos();
        return clienteDao.buscar(q.trim());
    }

    @Override @Transactional(readOnly = true)
    public Cliente getById(Long idCliente) {
        return clienteDao.findByIdConUsuario(idCliente).orElse(null);
    }

    @Override @Transactional
    public Cliente guardar(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override @Transactional
    public void eliminar(Long idCliente) {
        clienteDao.deleteById(idCliente);
    }

    @Override @Transactional(readOnly = true)
    public List<Usuario> usuariosDisponibles() {
        return usuarioDao.usuariosActivosSinCliente();
    }

    @Override @Transactional(readOnly = true)
    public List<Usuario> usuariosDisponiblesIncluyendo(Usuario actual) {
        List<Usuario> base = new ArrayList<>(usuarioDao.usuariosActivosSinCliente());
        if (actual != null && actual.getIdUsuario() != null) {
            boolean yaEsta = base.stream().anyMatch(u -> u.getIdUsuario().equals(actual.getIdUsuario()));
            if (!yaEsta) base.add(0, actual);
        }
        return base;
    }
}
