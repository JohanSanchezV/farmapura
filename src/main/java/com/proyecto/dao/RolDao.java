package com.proyecto.dao;

import com.proyecto.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolDao extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}