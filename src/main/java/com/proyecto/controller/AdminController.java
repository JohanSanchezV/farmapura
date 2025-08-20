package com.proyecto.controller;

import com.proyecto.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;

@GetMapping("/admin")
public String dashboard(Model model) {
    var k = dashboardService.getKpis();

    model.addAttribute("totalProductos",  k.getTotalProductos());
    model.addAttribute("porVencer",       k.getPorVencer());
    model.addAttribute("bajoReorden",     k.getBajoReorden());
    model.addAttribute("fechaUltimaVenta",k.getFechaUltimaVenta());

    model.addAttribute("active", "home"); 
    model.addAttribute("tituloPagina", "Admin · FarmaPura");
    model.addAttribute("view", "admin/dashboard :: content");
    return "layout/admin";
}


}