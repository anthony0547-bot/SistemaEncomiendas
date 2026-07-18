package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Comprobante;
import com.feedexx.encomiendas.entity.Envio;
import com.feedexx.encomiendas.service.ComprobanteService;
import com.feedexx.encomiendas.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/comprobantes")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    @Autowired
    private EnvioService envioService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listarComprobantes", comprobanteService.listarTodos());
        model.addAttribute("listarEnvios", envioService.listarTodos());
        return "comprobantes";
    }

    @PostMapping("/generar/{idEnvio}")
    public String generar(@PathVariable Long idEnvio) {
        Envio envio = envioService.buscarPorId(idEnvio);
        Comprobante comprobante = new Comprobante();
        comprobante.setNumero("COMP-" + System.currentTimeMillis());
        comprobante.setFecha(LocalDate.now());
        comprobante.setDistancia(envio.getRuta().getDistanciaKm());
        comprobante.setEnvio(envio);
        comprobanteService.guardar(comprobante);
        return "redirect:/comprobantes";
    }
}
