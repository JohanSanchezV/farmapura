package com.proyecto.controller;

import com.proyecto.domain.Proveedor;
import com.proyecto.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/proveedor")
public class AdminProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping("/listado")
    public String listado(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                          Model model) {

        String filtro = q == null ? "" : q.trim();

        var proveedores = filtro.isBlank()
                ? proveedorService.listarTodos()
                : proveedorService.buscar(filtro);

        model.addAttribute("proveedores", proveedores);
        model.addAttribute("filtroNombre", filtro);
        model.addAttribute("active", "proveedores");
        model.addAttribute("tituloVista", "Gestión de Proveedores");
        model.addAttribute("tituloPagina", "Proveedores · Admin");
        model.addAttribute("view", "admin/proveedor/listado :: content");
        return "layout/admin";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("active", "proveedores");
        model.addAttribute("tituloVista", "Nuevo Proveedor");
        model.addAttribute("tituloPagina", "Nuevo Proveedor · Admin");
        model.addAttribute("view", "admin/proveedor/modifica :: content");
        return "layout/admin";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long idProveedor, Model model) {
        var prov = proveedorService.getById(idProveedor);
        if (prov == null) return "redirect:/admin/proveedor/listado";

        model.addAttribute("proveedor", prov);
        model.addAttribute("active", "proveedores");
        model.addAttribute("tituloVista", "Editar Proveedor");
        model.addAttribute("tituloPagina", "Editar Proveedor · Admin");
        model.addAttribute("view", "admin/proveedor/modifica :: content");
        return "layout/admin";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proveedor proveedor,
                          @RequestParam(name = "imagenFile", required = false) MultipartFile imagenFile) {
        proveedorService.guardar(proveedor, imagenFile);
        return "redirect:/admin/proveedor/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long idProveedor) {
        proveedorService.eliminar(idProveedor);
        return "redirect:/admin/proveedor/listado";
    }
}
