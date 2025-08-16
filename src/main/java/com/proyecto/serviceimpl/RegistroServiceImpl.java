package com.proyecto.serviceimpl;

import com.proyecto.dao.ClienteDao;
import com.proyecto.dao.RolDao;
import com.proyecto.dao.UsuarioDao;
import com.proyecto.domain.Cliente;
import com.proyecto.domain.Usuario;
import com.proyecto.service.RegistroService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistroServiceImpl implements RegistroService {

    private final UsuarioDao usuarioDao;
    private final ClienteDao clienteDao;
    private final RolDao rolDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Usuario registrarCliente(Usuario usuario, Cliente cliente) {
        // 1) Codificar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);

        // 2) Asignar rol CLIENTE
        var rolCliente = rolDao.findByNombre("CLIENTE")
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no existe"));
        usuario.setRoles(Set.of(rolCliente));

        // 3) Guardar usuario
        usuario = usuarioDao.save(usuario);

        // 4) Vincular perfil de cliente
        cliente.setUsuario(usuario);
        clienteDao.save(cliente);

        return usuario;
    }
}
