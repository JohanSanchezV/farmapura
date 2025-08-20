package com.proyecto.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardDao extends CrudRepository<com.proyecto.domain.Producto, Long> {

    @Query(
        value = """
                SELECT 
                  total_productos   AS totalProductos,
                  bajo_reorden      AS bajoReorden,
                  por_vencer        AS porVencer,
                  fecha_ultima_venta AS fechaUltimaVenta
                FROM v_kpis_admin
                LIMIT 1
                """,
        nativeQuery = true
    )
    KpisAdminProjection getKpis();
}
