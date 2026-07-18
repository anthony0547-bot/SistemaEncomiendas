package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Envio;
import com.feedexx.encomiendas.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rastreo")
public class RastreoController {

    @Autowired
    private EnvioService envioService;

    @GetMapping
    public String mostrarFormulario() {
        return "rastreo";
    }

    @PostMapping("/buscar")
    public String buscar(@RequestParam Long idEnvio, Model model) {
        Envio envio = envioService.buscarPorId(idEnvio);

        if (envio == null) {
            model.addAttribute("noEncontrado", true);
        } else {
            model.addAttribute("envio", envio);
        }
        return "rastreo";
    }
}
