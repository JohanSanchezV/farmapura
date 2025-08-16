package com.proyecto.service.dto;

import com.proyecto.domain.Producto;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItem {

    private Producto producto;
    private int cantidad = 1;
    private BigDecimal precioUnitario;

    public CartItem(Producto producto) {
        this.producto = producto;
        this.precioUnitario = (producto != null && producto.getPrecio() != null)
                ? producto.getPrecio()
                : BigDecimal.ZERO;
        this.cantidad = 1;
    }

    public CartItem(Producto producto, int cantidad) {
        this.producto = producto;
        this.precioUnitario = (producto != null && producto.getPrecio() != null)
                ? producto.getPrecio()
                : BigDecimal.ZERO;
        this.cantidad = Math.max(1, cantidad);
    }

    public BigDecimal getSubtotal() {
        BigDecimal unit = (precioUnitario != null) ? precioUnitario : BigDecimal.ZERO;
        return unit.multiply(BigDecimal.valueOf(cantidad));
    }
}
