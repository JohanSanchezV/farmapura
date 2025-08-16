package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "factura")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Factura {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long idFactura;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Builder.Default
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal impuesto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 9)   // 'PENDIENTE' = 9
    private EstadoFactura estado = EstadoFactura.PAGADA;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "forma_pago", nullable = false, length = 13) // 'TRANSFERENCIA' = 13
    private FormaPago formaPago = FormaPago.EFECTIVO;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaDetalle> detalles;
}
