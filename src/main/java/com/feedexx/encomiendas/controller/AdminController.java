package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.service.CiudadService;
import com.feedexx.encomiendas.service.ComprobanteService;
import com.feedexx.encomiendas.service.EnvioService;
import com.feedexx.encomiendas.service.RepartidorService;
import com.feedexx.encomiendas.service.RutaService;
import com.feedexx.encomiendas.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
	

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private RutaService rutaService;

    @Autowired
    private RepartidorService repartidorService;

    @Autowired
    private ComprobanteService comprobanteService;

    @Autowired
    private EnvioService envioService;

    @GetMapping("/admin")
    public String panelAdministrador(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/";
        }

        model.addAttribute("usuario", usuario);

        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalCiudades", ciudadService.listarTodas().size());
        model.addAttribute("totalRutas", rutaService.listarTodas().size());
        model.addAttribute("totalRepartidores", repartidorService.listarTodos().size());
        model.addAttribute("totalComprobantes", comprobanteService.listarTodos().size());
        model.addAttribute("totalEnvios", envioService.listarTodos().size());
        

        return "admin";
    }
    @GetMapping("/admin/envios")
    public String administrarEnvios(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!usuario.getRol().equals("ADMIN")) {
            return "redirect:/";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("listarEnvios", envioService.listarTodos());
        model.addAttribute("listarRepartidores", repartidorService.listarDisponibles());	

        return "admin-envios";
    }
    @PostMapping("/admin/envios/entregar")
    public String entregarEnvio(@RequestParam Long idEnvio) {

        envioService.entregarEnvio(idEnvio);

        return "redirect:/admin/envios";
    }
    @PostMapping("/admin/envios/asignar")
    public String asignarRepartidor(@RequestParam Long idEnvio,
                                    @RequestParam Long idRepartidor) {

        envioService.asignarRepartidor(idEnvio, idRepartidor);

        return "redirect:/admin/envios";
    }
    

}