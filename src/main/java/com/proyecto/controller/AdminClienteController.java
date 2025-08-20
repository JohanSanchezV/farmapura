package com.proyecto.controller;

import com.proyecto.domain.Cliente;
import com.proyecto.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/admin/cliente", "/admin/clientes"})
public class AdminClienteController {

    private final ClienteService clienteService;

    @GetMapping({"", "/", "/listado"})
    public String listado(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                          Model model) {

        var clientes = q.isBlank() ? clienteService.listarTodos() : clienteService.buscar(q);

        model.addAttribute("clientes", clientes);
        model.addAttribute("filtroNombre", q);

        model.addAttribute("active", "cliente");         
        model.addAttribute("tituloVista", "Gestión de Clientes");
        model.addAttribute("tituloPagina", "Clientes · Admin");
        model.addAttribute("view", "admin/cliente/listado :: content");
        return "layout/admin";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("usuarios", clienteService.usuariosDisponibles());
        model.addAttribute("active", "cliente");       
        model.addAttribute("tituloPagina", "Nuevo Cliente · Admin");
        model.addAttribute("view", "admin/cliente/modifica :: content");
        return "layout/admin";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long idCliente, Model model) {
        var c = clienteService.getById(idCliente);
        if (c == null) return "redirect:/admin/cliente/listado";

        model.addAttribute("cliente", c);
        model.addAttribute("usuarios", clienteService.usuariosDisponiblesIncluyendo(c.getUsuario()));
        model.addAttribute("active", "cliente");       
        model.addAttribute("tituloPagina", "Editar Cliente · Admin");
        model.addAttribute("view", "admin/cliente/modifica :: content");
        return "layout/admin";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Cliente cliente,
                          BindingResult br,
                          Model model) {

        if (br.hasErrors() || cliente.getUsuario() == null || cliente.getUsuario().getIdUsuario() == null) {
            if (!br.hasErrors()) {
                br.rejectValue("usuario", "required", "Debe seleccionar un usuario.");
            }
            model.addAttribute("usuarios",
                    clienteService.usuariosDisponiblesIncluyendo(cliente.getUsuario()));
            model.addAttribute("active", "cliente");     
            model.addAttribute("tituloPagina",
                    (cliente.getIdCliente() == null) ? "Nuevo Cliente · Admin" : "Editar Cliente · Admin");
            model.addAttribute("view", "admin/cliente/modifica :: content");
            return "layout/admin";
        }

        clienteService.guardar(cliente);
        return "redirect:/admin/cliente/listado";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long idCliente) {
        clienteService.eliminar(idCliente);
        return "redirect:/admin/cliente/listado";
    }
}
