package com.proyecto.dao;

import com.proyecto.domain.InventarioSalida;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface InventarioSalidaDao extends JpaRepository<InventarioSalida, Long> {

    @Query("""
        SELECT s FROM InventarioSalida s
        LEFT JOIN s.producto p
        WHERE (:q IS NULL OR :q = ''
               OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(COALESCE(s.observaciones,'')) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(COALESCE(s.motivo,'')) LIKE LOWER(CONCAT('%', :q, '%'))
        )
        ORDER BY s.idSalida DESC
    """)
    List<InventarioSalida> buscar(@Param("q") String q);
}
