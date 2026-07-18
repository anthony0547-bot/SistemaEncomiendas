package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listarUsuarios", usuarioService.listarTodos());
        model.addAttribute("usuarioNuevo", new Usuario());
        return "usuarios";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuarioNuevo") Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuarioNuevo", usuarioService.buscarPorId(id));
        model.addAttribute("listarUsuarios", usuarioService.listarTodos());
        return "usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
