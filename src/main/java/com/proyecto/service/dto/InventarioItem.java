package com.proyecto.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InventarioItem {
    private Long idProducto;
    private String nombreProducto;
    private String categoria;  
    private Integer existencias;    
    private BigDecimal costoPromedio;
    private LocalDateTime ultimaEntrada;
    private LocalDateTime ultimaSalida;
}
