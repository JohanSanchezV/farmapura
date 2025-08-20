package com.proyecto.service;

import com.proyecto.domain.InventarioEntrada;
import com.proyecto.domain.InventarioSalida;
import com.proyecto.domain.Producto;
import com.proyecto.service.dto.InventarioItem;
import java.util.List;

public interface InventarioService {

    // Consulta (usa proyección)
    List<InventarioItem> consultaInventario(String q);

    // Entradas
    List<InventarioEntrada> listarEntradas(String q);
    InventarioEntrada getEntrada(Long idEntrada);
    InventarioEntrada guardarEntrada(InventarioEntrada entrada);
    void eliminarEntrada(Long idEntrada);

    // Salidas
    List<InventarioSalida> listarSalidas(String q);
    InventarioSalida getSalida(Long idSalida);
    InventarioSalida guardarSalida(InventarioSalida salida);
    void eliminarSalida(Long idSalida);
}
