package com.proyecto.serviceimpl;

import com.proyecto.dao.ClienteDao;
import com.proyecto.dao.FacturaDao;
import com.proyecto.dao.RolDao;
import com.proyecto.dao.UsuarioDao;
import com.proyecto.domain.Cliente;
import com.proyecto.domain.Usuario;
import com.proyecto.service.FirebaseStorageService;
import com.proyecto.service.UsuarioService;
import com.proyecto.service.dto.RegistroDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDao usuarioDao;
    private final RolDao rolDao;
    private final ClienteDao clienteDao;
    private final FacturaDao facturaDao; 
    private final PasswordEncoder passwordEncoder;
    private final FirebaseStorageService firebaseStorageService;

    @Transactional
    @Override
    public void registrarCliente(RegistroDto dto, MultipartFile imagenFile) {
        var u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setNombre(dto.getNombre());
        u.setApellidos(dto.getApellidos());
        u.setTelefono(dto.getTelefono());
        u.setActivo(true);
        usuarioDao.save(u);

        if (imagenFile != null && !imagenFile.isEmpty()) {
            String url = firebaseStorageService.cargaImagen(imagenFile, "usuarios", u.getIdUsuario());
            u.setRutaImagen(url);
            usuarioDao.save(u);
        }

        var rolCliente = rolDao.findByNombre("CLIENTE")
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no existe"));
        usuarioDao.asignarRol(u.getIdUsuario(), rolCliente.getIdRol());

        Cliente.TipoCedula tipoEnum = Cliente.TipoCedula.FISICA;
        if (dto.getTipoCedula() != null) {
            try { tipoEnum = Cliente.TipoCedula.valueOf(dto.getTipoCedula().toUpperCase()); }
            catch (IllegalArgumentException ignored) {}
        }

        var c = new Cliente();
        c.setUsuario(u);
        c.setTipoCedula(tipoEnum);
        c.setCedula(dto.getCedula());
        c.setDireccion(dto.getDireccion());
        c.setTelefonoAlt(dto.getTelefonoAlt());
        clienteDao.save(c);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> listarTodos() {
        return usuarioDao.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Usuario> buscar(String q) {
        return usuarioDao.buscar(q);
    }

    @Transactional(readOnly = true)
    @Override
    public Usuario getById(Long idUsuario) {
        return usuarioDao.findById(idUsuario).orElse(null);
    }

    @Transactional
    @Override
    public Usuario guardar(Usuario usuario, MultipartFile imagenFile, String passwordPlano, Long idRol) {
        Usuario target;

        if (usuario.getIdUsuario() != null) {
            target = usuarioDao.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuario.getIdUsuario()));

            target.setUsername(usuario.getUsername());
            target.setEmail(usuario.getEmail());
            target.setNombre(usuario.getNombre());
            target.setApellidos(usuario.getApellidos());
            target.setTelefono(usuario.getTelefono());
            target.setActivo(usuario.getActivo() != null ? usuario.getActivo() : target.getActivo());

            if (passwordPlano != null && !passwordPlano.isBlank()) {
                target.setPassword(passwordEncoder.encode(passwordPlano));
            }

            if (usuario.getRutaImagen() != null && (imagenFile == null || imagenFile.isEmpty())) {
                target.setRutaImagen(usuario.getRutaImagen());
            }
        } else {
            target = usuario;
            if (target.getActivo() == null) target.setActivo(Boolean.TRUE);

            if (passwordPlano == null || passwordPlano.isBlank()) {
                throw new IllegalArgumentException("La contraseña es obligatoria para crear usuarios.");
            }
            target.setPassword(passwordEncoder.encode(passwordPlano));
        }

        target = usuarioDao.save(target);

        if (imagenFile != null && !imagenFile.isEmpty()) {
            String url = firebaseStorageService.cargaImagen(imagenFile, "usuarios", target.getIdUsuario());
            target.setRutaImagen(url);
            target = usuarioDao.save(target);
        }

        if (idRol != null) {
            usuarioDao.limpiarRoles(target.getIdUsuario());
            usuarioDao.asignarRol(target.getIdUsuario(), idRol);
        }

        return target;
    }

    @Transactional
    @Override
    public boolean eliminar(Long idUsuario) {
        boolean tieneFacturas = facturaDao.existsByUsuario_IdUsuario(idUsuario);
        if (tieneFacturas) {
            usuarioDao.findById(idUsuario).ifPresent(u -> {
                u.setActivo(false);
                usuarioDao.save(u);
            });
            return false; 
        } else {
            usuarioDao.limpiarRoles(idUsuario);
            usuarioDao.deleteById(idUsuario);
            return true; 
        }
    }

    @Transactional
    @Override
    public void toggleActivo(Long idUsuario) {
        var u = usuarioDao.findById(idUsuario).orElseThrow();
        u.setActivo(!Boolean.TRUE.equals(u.getActivo()));
        usuarioDao.save(u);
    }
}
