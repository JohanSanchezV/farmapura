package com.proyecto.dao;

import com.proyecto.domain.Proveedor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProveedorDao extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByActivoTrue();

    Optional<Proveedor> findByCedula(String cedula);

    @Query("SELECT p FROM Proveedor p " +
           "WHERE p.activo = true AND (" +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
           "LOWER(p.cedula) LIKE LOWER(CONCAT('%', :filtro, '%'))" +
           ")")
    List<Proveedor> buscarActivos(@Param("filtro") String filtro);
}
