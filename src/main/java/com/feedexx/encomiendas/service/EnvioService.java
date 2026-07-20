package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Envio;
import com.feedexx.encomiendas.entity.Ruta;
import com.feedexx.encomiendas.repository.EnvioRepository;
import com.feedexx.encomiendas.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.feedexx.encomiendas.repository.RepartidorRepository;

@Service
public class EnvioService {
	@Autowired
	private RepartidorRepository repartidorRepository;

    @Autowired
    private EnvioRepository envioRepository;


    @Autowired
    private GrafoService grafoService;

    // Tarifas base para el calculo automatico del costo (RF04)
    private static final double COSTO_POR_KG = 0.75;

    public List<Envio> listarTodos() {
        return envioRepository.findAll();
    }
    public List<Envio> listarPorUsuario(Long idUsuario) {
        return envioRepository.findByUsuario_IdUsuario(idUsuario);
    }
    public List<Envio> listarPorRepartidor(Long idRepartidor){

        return envioRepository.findByRepartidor_IdRepartidor(idRepartidor);

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
    public double calcularCosto(double costoRuta, double pesoKg) {

        double recargoPeso = pesoKg * 0.75;

        return costoRuta + recargoPeso;

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

        envio.setCosto(
                calcularCosto(
                        rutaAsignada.getCostoEnvio(),
                        envio.getPeso()));

        return envioRepository.save(envio);
    }

    public Envio guardar(Envio envio) {
        return envioRepository.save(envio);
    }
    public void asignarRepartidor(Long idEnvio, Long idRepartidor) {

        Envio envio = buscarPorId(idEnvio);

        if (envio == null) {
            return;
        }

        var repartidor = repartidorRepository.findById(idRepartidor).orElse(null);

        if (repartidor == null) {
            return;
        }

        envio.setRepartidor(repartidor);
        envio.setEstado("EN_TRANSITO");

        repartidor.setEstado("EN_RUTA");

        repartidorRepository.save(repartidor);
        envioRepository.save(envio);
    }
    public void entregarEnvio(Long idEnvio) {

        Envio envio = buscarPorId(idEnvio);

        if (envio == null) {
            return;
        }

        if (envio.getRepartidor() != null) {

            envio.getRepartidor().setEstado("DISPONIBLE");

            repartidorRepository.save(envio.getRepartidor());
        }

        envio.setEstado("ENTREGADO");

        envioRepository.save(envio);
    }
}
