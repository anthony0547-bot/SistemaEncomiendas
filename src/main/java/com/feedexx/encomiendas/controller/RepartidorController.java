package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Repartidor;
import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.service.RepartidorService;
import com.feedexx.encomiendas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/repartidores")
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listarRepartidores", repartidorService.listarTodos());
        model.addAttribute("repartidorNuevo", new Repartidor());
        model.addAttribute("listarUsuarios", usuarioService.listarTodos());
        return "repartidores";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("repartidorNuevo") Repartidor repartidor,
                           @RequestParam Long idUsuario) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        repartidor.setUsuario(usuario);
        repartidorService.guardar(repartidor);
        return "redirect:/repartidores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("repartidorNuevo", repartidorService.buscarPorId(id));
        model.addAttribute("listarRepartidores", repartidorService.listarTodos());
        model.addAttribute("listarUsuarios", usuarioService.listarTodos());
        return "repartidores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repartidorService.eliminar(id);
        return "redirect:/repartidores";
    }
}
