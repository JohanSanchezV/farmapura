package com.proyecto.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter @Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cedula", nullable = false)
    private TipoCedula tipoCedula;

    @Column(name = "cedula", nullable = false, unique = true, length = 30)
    private String cedula;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono_alt")
    private String telefonoAlt;

    public enum TipoCedula { FISICA, JURIDICA }
}