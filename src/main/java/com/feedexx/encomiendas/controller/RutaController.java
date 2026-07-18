package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Ciudad;
import com.feedexx.encomiendas.entity.Ruta;
import com.feedexx.encomiendas.service.CiudadService;
import com.feedexx.encomiendas.service.OrsService;
import com.feedexx.encomiendas.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/rutas")
public class RutaController {

    @Autowired
    private RutaService rutaService;

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private OrsService orsService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listarRutas", rutaService.listarTodas());
        model.addAttribute("rutaNueva", new Ruta());
        model.addAttribute("listarCiudades", ciudadService.listarTodas());
        return "rutas";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("rutaNueva") Ruta ruta,
                           @RequestParam Long idCiudadOrigen,
                           @RequestParam Long idCiudadDestino) {
        Ciudad origen = ciudadService.buscarPorId(idCiudadOrigen);
        Ciudad destino = ciudadService.buscarPorId(idCiudadDestino);
        ruta.setCiudadOrigen(origen);
        ruta.setCiudadDestino(destino);
        rutaService.guardar(ruta);
        return "redirect:/rutas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        rutaService.eliminar(id);
        return "redirect:/rutas";
    }

    /**
     * Calcula distancia (km) y tiempo estimado (min) entre dos coordenadas
     * usando la API de OpenRouteService. Se usa solo desde el boton
     * "Calcular distancia" del formulario de Rutas; el mapa (Google Maps)
     * no se toca.
     */
    @GetMapping("/calcular-distancia")
    @ResponseBody
    public ResponseEntity<?> calcularDistancia(@RequestParam Double origenLat,
                                                @RequestParam Double origenLng,
                                                @RequestParam Double destinoLat,
                                                @RequestParam Double destinoLng) {
        try {
            OrsService.ResultadoDistancia resultado = orsService.calcularDistancia(
                    origenLat, origenLng, destinoLat, destinoLng);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("distanciaKm", resultado.distanciaKm);
            respuesta.put("tiempoMinutos", resultado.tiempoMinutos);
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No se pudo calcular la distancia con ORS: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}