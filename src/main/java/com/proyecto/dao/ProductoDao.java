package com.proyecto.dao;

import com.proyecto.domain.Producto;
import com.proyecto.service.dto.InventarioItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoDao extends JpaRepository<Producto, Long> {

    /**
     * Consulta de inventario uniendo con inventario_actual.
     * Si no hay fila en inventario_actual, usa producto.stock.
     */
    @Query(value = """
        SELECT 
            p.id_producto       AS idProducto,
            p.nombre            AS nombreProducto,
            c.nombre            AS nombreCategoria,
            p.precio            AS precio,
            COALESCE(ia.existencias, p.stock) AS stock
        FROM producto p
        LEFT JOIN categoria c        ON c.id_categoria = p.id_categoria
        LEFT JOIN inventario_actual ia ON ia.id_producto = p.id_producto
        WHERE (:q IS NULL OR :q = '' 
               OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')))
        ORDER BY p.id_producto
        """, nativeQuery = true)
    List<InventarioItem> consultaInventario(@Param("q") String q);

    List<Producto> findByActivoTrue();
}
