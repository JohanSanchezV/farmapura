package com.proyecto.dao;

import com.proyecto.domain.InventarioEntrada;
import com.proyecto.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventarioEntradaDao extends JpaRepository<InventarioEntrada, Long> {
    List<InventarioEntrada> findByProducto(Producto producto);
}
