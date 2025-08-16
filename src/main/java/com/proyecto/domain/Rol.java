package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "rol")
@Data @NoArgsConstructor @AllArgsConstructor
public class Rol {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(nullable = false, unique = true, length = 30)
    private String nombre; // ADMIN, CLIENTE, VENDEDOR, CONTABILIDAD
}