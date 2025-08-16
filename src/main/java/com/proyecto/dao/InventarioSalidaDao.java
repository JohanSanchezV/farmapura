package com.proyecto.dao;

import com.proyecto.domain.InventarioSalida;
import com.proyecto.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventarioSalidaDao extends JpaRepository<InventarioSalida, Long> {
    List<InventarioSalida> findByProducto(Producto producto);
}

