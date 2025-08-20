package com.proyecto.service.dto;

import java.math.BigDecimal;

public record InventarioResumenDto(
        Long idProducto,
        String nombreProducto,
        String nombreCategoria,
        BigDecimal precio,
        Integer stock
) {}
