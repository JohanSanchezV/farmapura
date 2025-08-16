package com.proyecto.dao;

import com.proyecto.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteDao extends JpaRepository<Cliente, Long> {
    boolean existsByCedula(String cedula);
}