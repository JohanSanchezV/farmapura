package com.proyecto.dao;

import com.proyecto.domain.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ClienteDao extends JpaRepository<Cliente, Long> {

    @Query("""
           SELECT c FROM Cliente c
           JOIN FETCH c.usuario u
           ORDER BY u.nombre, u.apellidos
           """)
    List<Cliente> findAllConUsuario();

    @Query("""
           SELECT c FROM Cliente c
           JOIN FETCH c.usuario u
           WHERE LOWER(u.nombre)    LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(u.email)     LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(u.username)  LIKE LOWER(CONCAT('%', :q, '%'))
              OR LOWER(c.cedula)    LIKE LOWER(CONCAT('%', :q, '%'))
           ORDER BY u.nombre, u.apellidos
           """)
    List<Cliente> buscar(@Param("q") String q);

    @Query("""
           SELECT c FROM Cliente c
           JOIN FETCH c.usuario u
           WHERE c.idCliente = :id
           """)
    Optional<Cliente> findByIdConUsuario(@Param("id") Long id);
}
