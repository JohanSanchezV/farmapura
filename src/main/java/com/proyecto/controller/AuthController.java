package com.proyecto.controller;

import com.proyecto.service.UsuarioService;
import com.proyecto.service.dto.RegistroDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registro", new RegistroDto());
        model.addAttribute("tituloVista", "Registrarse");
        model.addAttribute("tituloPagina", "Registro · Farmapura");
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registroSubmit(@ModelAttribute("registro") RegistroDto dto,
                                 @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
                                 RedirectAttributes ra) {
        usuarioService.registrarCliente(dto, imagenFile);
        ra.addFlashAttribute("msg", "Cuenta creada. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }
}
