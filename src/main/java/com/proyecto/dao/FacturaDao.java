package com.proyecto.dao;

import com.proyecto.domain.Factura;
import com.proyecto.domain.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FacturaDao extends JpaRepository<Factura, Long> {
    List<Factura> findByUsuario_IdUsuario(Long idUsuario);
    Optional<Factura> findTopByEstadoOrderByFechaDesc(EstadoFactura estado);
}
