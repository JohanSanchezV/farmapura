package com.proyecto.controller;

import com.proyecto.domain.Usuario;
import com.proyecto.service.RolService;
import com.proyecto.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping({"/admin/usuario", "/admin/usuarios"})
public class AdminUsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    @GetMapping({"", "/", "/listado"})
    public String listado(@RequestParam(name="q", required=false, defaultValue="") String q,
                          Model model) {
        List<Usuario> usuarios = q.isBlank()
                ? usuarioService.listarTodos()
                : usuarioService.buscar(q);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("q", q);
        model.addAttribute("active", "usuarios");
        model.addAttribute("tituloPagina", "Usuarios · Admin");
        model.addAttribute("view", "admin/usuario/listado :: content");
        return "layout/admin";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("active", "usuarios");
        model.addAttribute("tituloPagina", "Nuevo Usuario · Admin");
        model.addAttribute("view", "admin/usuario/modifica :: content");
        return "layout/admin";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable("id") Long idUsuario, Model model) {
        var u = usuarioService.getById(idUsuario);
        if (u == null) return "redirect:/admin/usuarios/listado";

        model.addAttribute("usuario", u);
        model.addAttribute("roles", rolService.listarTodos());
        model.addAttribute("active", "usuarios");
        model.addAttribute("tituloPagina", "Editar Usuario · Admin");
        model.addAttribute("view", "admin/usuario/modifica :: content");
        return "layout/admin";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Usuario usuario,
                          BindingResult br,
                          @RequestParam(name="idRol", required=false) Long idRol,
                          @RequestParam(name="imagenFile", required=false) MultipartFile imagenFile,
                          @RequestParam(name="passwordPlano", required=false) String passwordPlano,
                          Model model) {

        if (br.hasErrors()) {
            model.addAttribute("roles", rolService.listarTodos());
            model.addAttribute("active", "usuarios");
            model.addAttribute("tituloPagina",
                    (usuario.getIdUsuario()==null) ? "Nuevo Usuario · Admin" : "Editar Usuario · Admin");
            model.addAttribute("view", "admin/usuario/modifica :: content");
            return "layout/admin";
        }

        try {
            usuarioService.guardar(usuario, imagenFile, passwordPlano, idRol);
            return "redirect:/admin/usuarios/listado";
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("username")) {
                br.rejectValue("username", "duplicado", "Ya existe un usuario con ese username.");
            } else if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("email")) {
                br.rejectValue("email", "duplicado", "Ya existe un usuario con ese email.");
            } else {
                br.reject("error", "No se pudo guardar el usuario.");
            }
            model.addAttribute("roles", rolService.listarTodos());
            model.addAttribute("active", "usuarios");
            model.addAttribute("tituloPagina",
                    (usuario.getIdUsuario()==null) ? "Nuevo Usuario · Admin" : "Editar Usuario · Admin");
            model.addAttribute("view", "admin/usuario/modifica :: content");
            return "layout/admin";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long idUsuario, RedirectAttributes ra) {
        boolean borrado = usuarioService.eliminar(idUsuario);
        if (borrado) {
            ra.addFlashAttribute("ok", "Usuario eliminado correctamente.");
        } else {
            ra.addFlashAttribute("warn",
                "El usuario tiene facturas asociadas. Se desactivó en lugar de eliminarse.");
        }
        return "redirect:/admin/usuarios/listado";
    }

    @PostMapping("/toggle/{id}")
    public String toggleActivo(@PathVariable("id") Long idUsuario) {
        usuarioService.toggleActivo(idUsuario);
        return "redirect:/admin/usuarios/listado";
    }
}
