package com.proyecto.service;

import com.proyecto.domain.Factura;
import com.proyecto.service.dto.LineaVenta;
import java.util.List;

public interface FacturaService {
    Factura checkout(Long idUsuario, List<LineaVenta> lineas);
}
