package com.proyecto.controller;

import com.proyecto.domain.Producto;
import com.proyecto.service.ProductoService;
import com.proyecto.service.dto.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carrito")
@SessionAttributes("carrito")
public class CarritoController {

    private final ProductoService productoService;

    @ModelAttribute("carrito")
    public Map<Long, CartItem> carrito() {
        return new LinkedHashMap<>();
    }

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id,
                          @RequestParam(defaultValue = "1") int cantidad,
                          @ModelAttribute("carrito") Map<Long, CartItem> carrito) {
        Producto p = productoService.getById(id);
        if (p != null) {
            carrito.compute(id, (k, v) -> {
                if (v == null) return new CartItem(p, Math.max(1, cantidad));
                v.setCantidad(v.getCantidad() + Math.max(1, cantidad));
                return v;
            });
        }
        return "redirect:/carrito";
    }

    @GetMapping
    public String ver(@ModelAttribute("carrito") Map<Long, CartItem> carrito, Model model) {
        BigDecimal total = carrito.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("items", carrito.values());
        model.addAttribute("total", total);
        model.addAttribute("tituloVista", "Carrito");
        model.addAttribute("tituloPagina", "Carrito · Farmapura");
        return "market/carrito";
    }

    @PostMapping("/actualizar")
    public String actualizar(@RequestParam Map<String,String> params,
                             @ModelAttribute("carrito") Map<Long, CartItem> carrito) {
        params.forEach((k, v) -> {
            if (k.startsWith("qty_")) {
                Long id = Long.valueOf(k.substring(4));
                if (carrito.containsKey(id)) {
                    int cant = Math.max(0, Integer.parseInt(v));
                    if (cant == 0) carrito.remove(id);
                    else carrito.get(id).setCantidad(cant);
                }
            }
        });
        return "redirect:/carrito";
    }

    @PostMapping("/vaciar")
    public String vaciar(SessionStatus status) {
        status.setComplete();
        return "redirect:/carrito";
    }

    @PostMapping("/pagar")
    @PreAuthorize("isAuthenticated()") 
    public String pagar() {
        return "redirect:/mi-cuenta"; 
    }
}
