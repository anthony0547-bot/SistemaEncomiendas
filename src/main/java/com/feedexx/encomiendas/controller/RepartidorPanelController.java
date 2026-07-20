package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Repartidor;
import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.service.EnvioService;
import com.feedexx.encomiendas.service.RepartidorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RepartidorPanelController {

    @Autowired
    private RepartidorService repartidorService;

    @Autowired
    private EnvioService envioService;

    @GetMapping("/repartidor")
    public String panel(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null) {
            return "redirect:/login";
        }

        if (!"REPARTIDOR".equals(usuario.getRol())) {
            return "redirect:/";
        }

        Repartidor repartidor = repartidorService.buscarPorUsuario(usuario.getIdUsuario());

        if (repartidor == null) {
            model.addAttribute("mensajeError",
                    "Este usuario tiene el rol de repartidor, pero no tiene un registro asociado.");
            model.addAttribute("tipoError", "Repartidor no encontrado");
            return "error";
        }

        model.addAttribute("repartidor", repartidor);

        model.addAttribute("envios",
                envioService.listarPorRepartidor(repartidor.getIdRepartidor()));

        return "panel-repartidor";
    }

    @PostMapping("/repartidor/entregar")
    public String entregar(@RequestParam Long idEnvio) {

        envioService.entregarEnvio(idEnvio);

        return "redirect:/repartidor";
    }

}