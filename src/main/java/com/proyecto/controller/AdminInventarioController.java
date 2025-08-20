package com.proyecto.controller;

import com.proyecto.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/inventario")
public class AdminInventarioController {

    private final InventarioService inventarioService;

    @GetMapping({"", "/", "/consulta"})
    public String consulta(@RequestParam(name="q", required=false, defaultValue="") String q,
                           Model model) {
        model.addAttribute("items", inventarioService.consultaInventario(q));
        model.addAttribute("q", q);
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Inventario · Admin");
        model.addAttribute("view", "admin/inventario/consulta :: content");
        return "layout/admin";
    }
}
