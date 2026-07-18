package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Cliente;
import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.service.ClienteService;
import com.feedexx.encomiendas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listarClientes", clienteService.listarTodos());
        model.addAttribute("clienteNuevo", new Cliente());
        model.addAttribute("listarUsuarios", usuarioService.listarTodos());
        return "clientes";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("clienteNuevo") Cliente cliente,
                           @RequestParam Long idUsuario) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        cliente.setUsuario(usuario);
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        model.addAttribute("clienteNuevo", clienteService.buscarPorId(id));
        model.addAttribute("listarClientes", clienteService.listarTodos());
        model.addAttribute("listarUsuarios", usuarioService.listarTodos());
        return "clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@org.springframework.web.bind.annotation.PathVariable Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}
