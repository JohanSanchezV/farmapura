package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "proveedor")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Proveedor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 30)
    private String cedula;

    @Column(length = 120)
    private String correo;

    @Column(length = 25)
    private String telefono;

    @Column(length = 400)
    private String direccion;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;
}
