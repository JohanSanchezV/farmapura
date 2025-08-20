package com.proyecto.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventario_actual")
@Getter @Setter
public class InventarioActual {

    @Id
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "existencias")
    private Integer existencias;

    @Column(name = "costo_promedio")
    private BigDecimal costoPromedio;

    @Column(name = "ultima_entrada")
    private LocalDateTime ultimaEntrada;

    @Column(name = "ultima_salida")
    private LocalDateTime ultimaSalida;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private Producto producto;
}
