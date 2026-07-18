package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Ciudad;
import com.feedexx.encomiendas.service.CiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ciudades")
public class CiudadController {

    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listarCiudades", ciudadService.listarTodas());
        model.addAttribute("ciudadNueva", new Ciudad());
        return "ciudades";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("ciudadNueva") Ciudad ciudad) {
        ciudadService.guardar(ciudad);
        return "redirect:/ciudades";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("ciudadNueva", ciudadService.buscarPorId(id));
        model.addAttribute("listarCiudades", ciudadService.listarTodas());
        return "ciudades";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        ciudadService.eliminar(id);
        return "redirect:/ciudades";
    }
}
