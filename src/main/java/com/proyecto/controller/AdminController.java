package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("tituloVista", "Panel de Administración");
        model.addAttribute("tituloPagina", "Admin · Farmapura");
        return "admin/index";
    }
}

