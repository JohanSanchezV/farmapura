package com.proyecto.controller;

import com.proyecto.domain.Categoria;
import com.proyecto.domain.Producto;
import com.proyecto.service.CategoriaService;
import com.proyecto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TiendaController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        List<Producto> destacados = productoService.listarActivos();
        if (destacados.size() > 8) {
            destacados = destacados.subList(0, 8);
        }

        model.addAttribute("tituloVista", "Bienvenido a Farmapura");
        model.addAttribute("tituloPagina", "Inicio · Farmapura");
        model.addAttribute("destacados", destacados);
        return "market/index";
    }

    @GetMapping("/productos")
    public String productos(@RequestParam(name = "categoria", required = false) Long idCategoria,
                            @RequestParam(name = "q", required = false, defaultValue = "") String q,
                            Model model) {

        List<Producto> productos;
        if (idCategoria != null) {
            var cat = new Categoria();
            cat.setIdCategoria(idCategoria);
            productos = productoService.listarPorCategoria(cat);
        } else if (!q.isBlank()) {
            productos = productoService.buscar(q);
        } else {
            productos = productoService.listarActivos();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("filtroNombre", q);
        model.addAttribute("filtroCategoria", idCategoria);
        model.addAttribute("tituloVista", "Productos");
        model.addAttribute("tituloPagina", "Productos · Farmapura");
        return "market/productos";
    }
}
