package com.proyecto.dao;

import com.proyecto.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);

    @Modifying
    @Query(value = "INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (?1, ?2)", nativeQuery = true)
    void asignarRol(Long idUsuario, Long idRol);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM usuario_rol WHERE id_usuario = ?1", nativeQuery = true)
    void limpiarRoles(Long idUsuario);

    @Query("""
        SELECT u FROM Usuario u
        WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', ?1, '%'))
           OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', ?1, '%'))
           OR LOWER(u.username) LIKE LOWER(CONCAT('%', ?1, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', ?1, '%'))
    """)
    List<Usuario> buscar(String q);
    
        @Query("""
           SELECT u FROM Usuario u
           WHERE u.activo = true
             AND NOT EXISTS (SELECT 1 FROM Cliente c WHERE c.usuario.idUsuario = u.idUsuario)
           ORDER BY u.nombre, u.apellidos
           """)
    List<Usuario> usuariosActivosSinCliente();
}
