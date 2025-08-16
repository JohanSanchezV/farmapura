package com.proyecto.dao;

import com.proyecto.domain.FacturaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacturaDetalleDao extends JpaRepository<FacturaDetalle, Long> {
    List<FacturaDetalle> findByFactura_IdFactura(Long idFactura);
}

