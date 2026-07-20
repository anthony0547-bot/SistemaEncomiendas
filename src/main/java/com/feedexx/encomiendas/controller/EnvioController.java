package com.feedexx.encomiendas.controller;

import com.feedexx.encomiendas.entity.Envio;
import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.service.CiudadService;
import com.feedexx.encomiendas.service.EnvioService;
import com.feedexx.encomiendas.service.OrsService;
import com.feedexx.encomiendas.service.RepartidorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private RepartidorService repartidorService;

    @Autowired
    private CiudadService ciudadService;

    @Autowired
    private OrsService orsService;

    @GetMapping
    public String listar(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        boolean esAdmin = false;

        if (usuario != null) {

            if ("ADMIN".equals(usuario.getRol())) {

                esAdmin = true;
                model.addAttribute("listarEnvios", envioService.listarTodos());

            } else {

                model.addAttribute("listarEnvios",
                        envioService.listarPorUsuario(usuario.getIdUsuario()));

            }

        } else {

            model.addAttribute("listarEnvios", java.util.Collections.emptyList());

        }

        model.addAttribute("esAdmin", esAdmin);
        model.addAttribute("envioNuevo", new Envio());
        model.addAttribute("listarCiudades", ciudadService.listarTodas());

        return "envios";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("envioNuevo") Envio envio,
                           @RequestParam Long idCiudadOrigen,
                           @RequestParam Long idCiudadDestino,
                           HttpSession session,
                           Model model) {

        // Solo al momento de ENVIAR se exige haber iniciado sesion.
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");

        if (usuarioActual == null) {
            return "redirect:/login";
        }

        // --- CORRECCIÓN PARA RAILWAY ---
        // Evita el error 'null value in column dimensiones' asignando un valor por defecto si no viene en el formulario
        if (envio.getDimensiones() == null || envio.getDimensiones().trim().isEmpty()) {
            envio.setDimensiones("Estándar");
        }

        envio.setFecha(LocalDate.now());
        envio.setEstado("REGISTRADO");
        envio.setUsuario(usuarioActual);

        try {
            envioService.registrarEnvio(envio, idCiudadOrigen, idCiudadDestino);
        } catch (IllegalStateException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("listarEnvios", envioService.listarTodos());
            model.addAttribute("envioNuevo", new Envio());
            model.addAttribute("listarCiudades", ciudadService.listarTodas());
            return "envios";
        }

        return "redirect:/envios?exito";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        envioService.eliminar(id);
        return "redirect:/envios";
    }

    @GetMapping("/mis-envios")
    public String misEnvios(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("listarEnvios",
                envioService.listarPorUsuario(usuario.getIdUsuario()));

        return "mis-envios";
    }

    /**
     * Endpoint AJAX consumido por envios.html: devuelve la geometria real de
     * la carretera (para dibujarla en el mapa) mas distancia y duracion,
     * usando OpenRouteService desde el backend.
     */
    @GetMapping("/trazar-ruta")
    @ResponseBody
    public Map<String, Object> trazarRuta(@RequestParam Long idOrigen,
                                           @RequestParam Long idDestino) {
        Map<String, Object> respuesta = new HashMap<>();
        var origen = ciudadService.buscarPorId(idOrigen);
        var destino = ciudadService.buscarPorId(idDestino);

        if (origen == null || destino == null) {
            respuesta.put("exito", false);
            respuesta.put("mensaje", "Ciudad no encontrada.");
            return respuesta;
        }

        var resultado = orsService.calcularRutaCompleta(
                origen.getLatitud(), origen.getLongitud(),
                destino.getLatitud(), destino.getLongitud()
        );

        if (!resultado.exito) {
            respuesta.put("exito", false);
            respuesta.put("mensaje", resultado.mensajeError);
            return respuesta;
        }

        respuesta.put("exito", true);
        respuesta.put("distanciaKm", resultado.distanciaKm);
        respuesta.put("tiempoMinutos", resultado.tiempoMinutos);
        respuesta.put("puntosRuta", resultado.puntosRuta);
        return respuesta;
    }
}