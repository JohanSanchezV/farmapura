package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "inventario_entrada")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InventarioEntrada {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Long idEntrada;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "costo_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @Column(name = "precio_venta_sug", precision = 10, scale = 2)
    private BigDecimal precioVentaSug;

    @Column(name = "punto_reorden_ref")
    private Integer puntoReordenRef;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(length = 500)
    private String observaciones;
}
