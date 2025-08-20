package com.proyecto.controller;

import com.proyecto.domain.Categoria;
import com.proyecto.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categoria")
public class AdminCategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("categorias", categoriaService.getCategorias(false));
        model.addAttribute("active", "productos"); 
        model.addAttribute("tituloPagina", "Categorías · Admin");
        model.addAttribute("view", "admin/categoria/listado :: content");
        return "layout/admin";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("active", "productos");
        model.addAttribute("tituloPagina", "Nueva Categoría · Admin");
        model.addAttribute("view", "admin/categoria/modifica :: content");
        return "layout/admin";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long idCategoria, Model model) {
        var categoria = categoriaService.getById(idCategoria);
        if (categoria == null) return "redirect:/admin/categoria/listado";

        model.addAttribute("categoria", categoria);
        model.addAttribute("active", "productos");
        model.addAttribute("tituloPagina", "Editar Categoría · Admin");
        model.addAttribute("view", "admin/categoria/modifica :: content");
        return "layout/admin";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria,
                          @RequestParam(name = "imagenFile", required = false) MultipartFile imagenFile) {
        categoriaService.guardar(categoria, imagenFile);
        return "redirect:/admin/categoria/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long idCategoria) {
        categoriaService.eliminar(idCategoria);
        return "redirect:/admin/categoria/listado";
    }
}
