package com.proyecto.dao;

import com.proyecto.domain.InventarioEntrada;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioEntradaDao extends JpaRepository<InventarioEntrada, Long> {

    @Query("""
           SELECT e
           FROM InventarioEntrada e
           LEFT JOIN e.producto p
           LEFT JOIN e.proveedor pr
           WHERE (:q IS NULL OR :q = '' OR
                  LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(COALESCE(pr.nombre, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR
                  LOWER(COALESCE(e.observaciones, '')) LIKE LOWER(CONCAT('%', :q, '%')))
           ORDER BY e.fecha DESC
           """)
    List<InventarioEntrada> buscar(@Param("q") String q);

    List<InventarioEntrada> findAllByOrderByFechaDesc();
}
