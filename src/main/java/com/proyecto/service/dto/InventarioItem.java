package com.proyecto.service.dto;

import java.math.BigDecimal;

public interface InventarioItem {
    Long getIdProducto();
    String getNombreProducto();
    String getNombreCategoria();
    BigDecimal getPrecio();
    Integer getStock();
}
