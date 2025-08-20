package com.proyecto.controller;

import com.proyecto.domain.InventarioEntrada;
import com.proyecto.domain.InventarioSalida;
import com.proyecto.service.InventarioService;
import com.proyecto.service.ProductoService;
import com.proyecto.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/inventario")
public class AdminInventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;
    private final ProveedorService proveedorService;

    /* -------- MENÚ -------- */
    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Inventario · Admin");
        model.addAttribute("view", "admin/inventario/index :: content");
        return "layout/admin";
    }

    /* -------- CONSULTA -------- */
    @GetMapping("/consulta")
    public String consulta(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                           Model model) {
        model.addAttribute("items", inventarioService.consultaInventario(q));
        model.addAttribute("filtro", q);
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Consulta de Inventario · Admin");
        model.addAttribute("view", "admin/inventario/consulta :: content");
        return "layout/admin";
    }

    /* ========================== ENTRADAS ========================== */

    @GetMapping("/entradas")
    public String entradas(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                           Model model) {
        model.addAttribute("entradas", inventarioService.listarEntradas(q));
        model.addAttribute("filtro", q);
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Entradas de Inventario · Admin");
        model.addAttribute("view", "admin/inventario/entrada/listado :: content");
        return "layout/admin";
    }

    @GetMapping("/entradas/nuevo")
    public String nuevaEntrada(Model model) {
        model.addAttribute("entrada", new InventarioEntrada());
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Nueva Entrada · Admin");
        model.addAttribute("view", "admin/inventario/entrada/modifica :: content");
        return "layout/admin";
    }

    @GetMapping("/entradas/modificar/{id}")
    public String modificarEntrada(@PathVariable("id") Long idEntrada, Model model) {
        var e = inventarioService.getEntrada(idEntrada);
        if (e == null) return "redirect:/admin/inventario/entradas";

        model.addAttribute("entrada", e);
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Editar Entrada · Admin");
        model.addAttribute("view", "admin/inventario/entrada/modifica :: content");
        return "layout/admin";
    }

    @PostMapping("/entradas/guardar")
    public String guardarEntrada(@Valid @ModelAttribute("entrada") InventarioEntrada entrada,
                                 BindingResult br,
                                 Model model) {

        if (entrada.getProducto() == null || entrada.getProducto().getIdProducto() == null) {
            br.rejectValue("producto", "required", "Debe seleccionar un producto.");
        }

        if (br.hasErrors()) {
            model.addAttribute("productos", productoService.listarActivos());
            model.addAttribute("proveedores", proveedorService.listarActivos());
            model.addAttribute("active", "inventario");
            model.addAttribute("tituloPagina",
                    entrada.getIdEntrada() == null ? "Nueva Entrada · Admin" : "Editar Entrada · Admin");
            model.addAttribute("view", "admin/inventario/entrada/modifica :: content");
            return "layout/admin";
        }

        inventarioService.guardarEntrada(entrada); 
        return "redirect:/admin/inventario/entradas";
    }

    @PostMapping("/entradas/eliminar/{id}")
    public String eliminarEntrada(@PathVariable("id") Long idEntrada) {
        inventarioService.eliminarEntrada(idEntrada);
        return "redirect:/admin/inventario/entradas";
    }

    /* ========================== SALIDAS ========================== */

    @GetMapping("/salidas")
    public String salidas(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                          Model model) {
        model.addAttribute("salidas", inventarioService.listarSalidas(q));
        model.addAttribute("filtro", q);
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Salidas de Inventario · Admin");
        model.addAttribute("view", "admin/inventario/salida/listado :: content");
        return "layout/admin";
    }

    @GetMapping("/salidas/nuevo")
    public String nuevaSalida(Model model) {
        model.addAttribute("salida", new InventarioSalida());
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Nueva Salida · Admin");
        model.addAttribute("view", "admin/inventario/salida/modifica :: content");
        return "layout/admin";
    }

    @GetMapping("/salidas/modificar/{id}")
    public String modificarSalida(@PathVariable("id") Long idSalida, Model model) {
        var s = inventarioService.getSalida(idSalida);
        if (s == null) return "redirect:/admin/inventario/salidas";

        model.addAttribute("salida", s);
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("active", "inventario");
        model.addAttribute("tituloPagina", "Editar Salida · Admin");
        model.addAttribute("view", "admin/inventario/salida/modifica :: content");
        return "layout/admin";
    }

    @PostMapping("/salidas/guardar")
    public String guardarSalida(@Valid @ModelAttribute("salida") InventarioSalida salida,
                                BindingResult br,
                                Model model) {
        if (salida.getProducto() == null || salida.getProducto().getIdProducto() == null) {
            br.rejectValue("producto", "required", "Debe seleccionar un producto.");
        }

        if (br.hasErrors()) {
            model.addAttribute("productos", productoService.listarActivos());
            model.addAttribute("active", "inventario");
            model.addAttribute("tituloPagina",
                    salida.getIdSalida() == null ? "Nueva Salida · Admin" : "Editar Salida · Admin");
            model.addAttribute("view", "admin/inventario/salida/modifica :: content");
            return "layout/admin";
        }

        inventarioService.guardarSalida(salida); 
        return "redirect:/admin/inventario/salidas";
    }

    @PostMapping("/salidas/eliminar/{id}")
    public String eliminarSalida(@PathVariable("id") Long idSalida) {
        inventarioService.eliminarSalida(idSalida);
        return "redirect:/admin/inventario/salidas";
    }
}
