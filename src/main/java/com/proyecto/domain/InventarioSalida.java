package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "inventario_salida")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InventarioSalida {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salida")
    private Long idSalida;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10) 
    private MotivoSalida motivo;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(length = 500)
    private String observaciones;
}
