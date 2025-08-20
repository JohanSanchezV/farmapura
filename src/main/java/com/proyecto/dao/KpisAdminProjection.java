package com.proyecto.dao;

import java.time.LocalDateTime;

public interface KpisAdminProjection {
    Long getTotalProductos();
    Long getBajoReorden();
    Long getPorVencer();
    LocalDateTime getFechaUltimaVenta();
}
