package com.proyecto.dao;
import com.proyecto.domain.Producto;
import com.proyecto.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoDao extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaAndActivoTrue(Categoria categoria);
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String filtro);
}

