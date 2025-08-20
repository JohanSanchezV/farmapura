package com.proyecto.dao;

import com.proyecto.domain.InventarioActual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioActualDao extends JpaRepository<InventarioActual, Long> {
}
