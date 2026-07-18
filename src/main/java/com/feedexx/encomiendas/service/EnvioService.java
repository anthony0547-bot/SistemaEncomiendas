package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Envio;
import com.feedexx.encomiendas.entity.Ruta;
import com.feedexx.encomiendas.repository.EnvioRepository;
import com.feedexx.encomiendas.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private GrafoService grafoService;

    // Tarifas base para el calculo automatico del costo (RF04)
    private static final double COSTO_BASE = 2.0;
    private static final double COSTO_POR_KG = 0.75;
    private static final double COSTO_POR_KM = 0.05;

    public List<Envio> listarTodos() {
        return envioRepository.findAll();
    }

    public Envio buscarPorId(Long id) {
        return envioRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        envioRepository.deleteById(id);
    }

    /**
     * Calcula el costo de un envio en base al peso del paquete y a la
     * distancia real de la ruta (obtenida del grafo, no de una tarifa fija).
     */
    public double calcularCosto(double pesoKg, double distanciaKm) {
        return COSTO_BASE + (pesoKg * COSTO_POR_KG) + (distanciaKm * COSTO_POR_KM);
    }

    /**
     * Registra un envio calculando previamente la ruta optima (Dijkstra)
     * entre la ciudad de origen y destino, y el costo segun peso/distancia.
     */
    public Envio registrarEnvio(Envio envio, Long idCiudadOrigen, Long idCiudadDestino) {
        GrafoService.ResultadoRuta resultado = grafoService.calcularCaminoMasCorto(idCiudadOrigen, idCiudadDestino);

        if (!resultado.existeCamino) {
            throw new IllegalStateException("No existe una ruta disponible entre las ciudades seleccionadas.");
        }

        // Se usa el primer tramo de la ruta optima como la Ruta asociada al envio.
        // (En un catalogo con rutas directas por ciudad, esto normalmente sera 1 solo tramo).
        Ruta rutaAsignada = resultado.rutasUsadas.get(0);
        envio.setRuta(rutaAsignada);
        envio.setCosto(calcularCosto(envio.getPeso(), resultado.distanciaTotal));

        return envioRepository.save(envio);
    }

    public Envio guardar(Envio envio) {
        return envioRepository.save(envio);
    }
}
