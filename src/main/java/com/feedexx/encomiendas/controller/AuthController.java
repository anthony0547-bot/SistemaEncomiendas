package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                 @RequestParam String contrasena,
                                 HttpSession session,
                                 Model model) {

        Usuario usuario = usuarioService.autenticar(correo, contrasena);

        if (usuario == null) {
            model.addAttribute("error", "ID de usuario o contraseña incorrectos.");
            return "login";
        }

        session.setAttribute("usuarioActual", usuario);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
