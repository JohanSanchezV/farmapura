package com.proyecto.controller;

import com.proyecto.domain.Categoria;
import com.proyecto.domain.Producto;
import com.proyecto.service.CategoriaService;
import com.proyecto.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/producto")
public class AdminProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping("/listado")
    public String listado(@RequestParam(name = "idCategoria", required = false) Long idCategoria,
                          @RequestParam(name = "q", required = false, defaultValue = "") String filtroNombre,
                          Model model) {

        List<Producto> productos;
        if (idCategoria != null) {
            var cat = new Categoria();
            cat.setIdCategoria(idCategoria);
            productos = productoService.listarPorCategoria(cat);
        } else if (!filtroNombre.isBlank()) {
            productos = productoService.buscar(filtroNombre);
        } else {
            productos = productoService.listarActivos();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("filtroNombre", filtroNombre);
        model.addAttribute("filtroCategoria", idCategoria);

        model.addAttribute("active", "productos");
        model.addAttribute("tituloPagina", "Productos · Admin");
        model.addAttribute("view", "admin/producto/listado :: content");
        return "layout/admin";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("active", "productos");
        model.addAttribute("tituloPagina", "Nuevo Producto · Admin");
        model.addAttribute("view", "admin/producto/modifica :: content");
        return "layout/admin";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long idProducto, Model model) {
        var p = productoService.getById(idProducto);
        if (p == null) return "redirect:/admin/producto/listado";

        model.addAttribute("producto", p);
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("active", "productos");
        model.addAttribute("tituloPagina", "Editar Producto · Admin");
        model.addAttribute("view", "admin/producto/modifica :: content");
        return "layout/admin";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Producto producto,
                          BindingResult br,
                          @RequestParam(name="imagenFile", required=false) MultipartFile imagenFile,
                          Model model) {
      if (br.hasErrors()) {
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("tituloVista", (producto.getIdProducto()==null) ? "Nuevo Producto" : "Editar Producto");
        model.addAttribute("tituloPagina", (producto.getIdProducto()==null) ? "Nuevo Producto · Admin" : "Editar Producto · Admin");
        return "admin/producto/modifica";
      }
      try {
        productoService.guardar(producto, imagenFile);
        return "redirect:/admin/producto/listado";
      } catch (org.springframework.dao.DataIntegrityViolationException ex) {
        br.rejectValue("nombre", "duplicado", "Ya existe un producto con ese nombre.");
        model.addAttribute("categorias", categoriaService.getCategorias(true));
        model.addAttribute("tituloVista", (producto.getIdProducto()==null) ? "Nuevo Producto" : "Editar Producto");
        model.addAttribute("tituloPagina", (producto.getIdProducto()==null) ? "Nuevo Producto · Admin" : "Editar Producto · Admin");
        return "admin/producto/modifica";
      }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long idProducto) {
        productoService.eliminar(idProducto);
        return "redirect:/admin/producto/listado";
    }
}
