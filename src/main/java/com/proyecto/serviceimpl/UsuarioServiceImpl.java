package com.proyecto.serviceimpl;

import com.proyecto.dao.ClienteDao;
import com.proyecto.dao.RolDao;
import com.proyecto.dao.UsuarioDao;
import com.proyecto.domain.Cliente;
import com.proyecto.domain.Usuario;
import com.proyecto.service.FirebaseStorageService;
import com.proyecto.service.UsuarioService;
import com.proyecto.service.dto.RegistroDto;
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
    private final PasswordEncoder passwordEncoder;
    private final FirebaseStorageService firebaseStorageService;

    @Transactional
    @Override
    public void registrarCliente(RegistroDto dto, MultipartFile imagenFile) {
        // 1) Usuario
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

        // 3) Rol CLIENTE
        var rolCliente = rolDao.findByNombre("CLIENTE")
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no existe"));
        usuarioDao.asignarRol(u.getIdUsuario(), rolCliente.getIdRol());

        // 4) Perfil Cliente
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
}