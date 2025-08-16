package com.proyecto.dao;

import com.proyecto.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);

    @Modifying
    @Query(value = "INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (?1, ?2)", nativeQuery = true)
    void asignarRol(Long idUsuario, Long idRol);
}