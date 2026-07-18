package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Ciudad;
import com.feedexx.encomiendas.entity.Ruta;
import com.feedexx.encomiendas.service.CiudadService;
import com.feedexx.encomiendas.service.GrafoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador dedicado a demostrar el motor de grafos (Criterios 3 y 4 de la
 * rubrica): permite elegir una ciudad de origen y una de destino y calcula,
 * usando Dijkstra sobre la lista de adyacencia, el camino mas corto entre
 * ambas -- incluso si no existe una ruta directa y hay que pasar por
 * ciudades intermedias.
 */
@Controller
@RequestMapping("/grafo")
public class GrafoController {

    @Autowired
    private GrafoService grafoService;

    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("listarCiudades", ciudadService.listarTodas());
        return "grafo";
    }

    @PostMapping("/buscar")
    public String buscarMejorRuta(@RequestParam Long idCiudadOrigen,
                                   @RequestParam Long idCiudadDestino,
                                   Model model) {

        model.addAttribute("listarCiudades", ciudadService.listarTodas());

        GrafoService.ResultadoRuta resultado = grafoService.calcularCaminoMasCorto(idCiudadOrigen, idCiudadDestino);

        if (!resultado.existeCamino) {
            model.addAttribute("sinRuta", true);
            return "grafo";
        }

        // Convertimos la lista de IDs del camino en objetos Ciudad completos
        // (con nombre, lat y lng) para poder mostrarlos y dibujarlos en el mapa.
        List<Ciudad> ciudadesDelCamino = new ArrayList<>();
        for (Long idCiudad : resultado.caminoCiudades) {
            ciudadesDelCamino.add(ciudadService.buscarPorId(idCiudad));
        }

        List<Ruta> tramos = resultado.rutasUsadas;

        model.addAttribute("ciudadesDelCamino", ciudadesDelCamino);
        model.addAttribute("tramos", tramos);
        model.addAttribute("distanciaTotal", resultado.distanciaTotal);
        model.addAttribute("resultadoEncontrado", true);

        return "grafo";
    }
}
