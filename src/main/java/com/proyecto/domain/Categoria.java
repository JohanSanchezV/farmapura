package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "categoria")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Categoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "ruta_imagen", length = 512)
    private String rutaImagen;

    @Column(nullable = false)
    private Boolean activo = true;
}
