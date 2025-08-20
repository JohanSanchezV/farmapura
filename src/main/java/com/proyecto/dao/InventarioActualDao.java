package com.proyecto.dao;

import com.proyecto.domain.InventarioActual;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface InventarioActualDao extends JpaRepository<InventarioActual, Long> {

    @Query("""
      SELECT ia
      FROM InventarioActual ia
      JOIN Producto p ON p.idProducto = ia.idProducto
      WHERE (:q = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')))
      ORDER BY p.nombre
    """)
    List<InventarioActual> buscarPorNombreProducto(@Param("q") String q);
}
