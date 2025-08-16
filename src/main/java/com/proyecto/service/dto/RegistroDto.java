package com.proyecto.service.dto;

import lombok.Data;

@Data
public class RegistroDto {
    private String tipoCedula;   // "FISICA" o "JURIDICA"
    private String cedula;

    private String username;
    private String email;
    private String password;

    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;
    private String telefonoAlt;
}