package com.feedexx.encomiendas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Locale;

/**
 * Servicio que consulta la API de OpenRouteService (HEiGIT) para obtener la
 * distancia y duracion REAL por carretera entre dos coordenadas.
 *
 * Se llama desde el backend (no desde el navegador) para evitar problemas de
 * CORS y para no exponer la API key en el HTML.
 */
@Service
public class OrsService {

    @Value("${ors.api.key}")
    private String apiKey;

    private static final String ORS_MATRIX_URL = "https://api.openrouteservice.org/v2/matrix/driving-car";

    public static class ResultadoDistancia {
        public double distanciaKm;
        public int tiempoMinutos;
        public boolean exito;
        public String mensajeError;
    }

    /**
     * Resultado con la geometria de la ruta real (lista de puntos lat/lng que
     * siguen la carretera), ademas de distancia y duracion -- todo en una
     * sola llamada a ORS Directions.
     */
    public static class ResultadoRutaCompleta {
        public double distanciaKm;
        public int tiempoMinutos;
        public java.util.List<double[]> puntosRuta = new java.util.ArrayList<>(); // cada punto: [lat, lng]
        public boolean exito;
        public String mensajeError;
    }

    public ResultadoDistancia calcularDistancia(double latOrigen, double lngOrigen,
                                                 double latDestino, double lngDestino) {
        ResultadoDistancia resultado = new ResultadoDistancia();
        try {
            // ORS espera las coordenadas como [longitud, latitud] (al reves que en el mapa)
            String cuerpoJson = String.format(Locale.US,
                    "{\"locations\":[[%f,%f],[%f,%f]],\"metrics\":[\"distance\",\"duration\"],\"units\":\"km\"}",
                    lngOrigen, latOrigen, lngDestino, latDestino);

            HttpClient cliente = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(ORS_MATRIX_URL))
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(cuerpoJson))
                    .build();

            HttpResponse<String> respuesta = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() != 200) {
                resultado.exito = false;
                resultado.mensajeError = "Error ORS (" + respuesta.statusCode() + "): " + respuesta.body();
                return resultado;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode raiz = mapper.readTree(respuesta.body());

            double distancia = raiz.get("distances").get(0).get(1).asDouble();
            double duracionSegundos = raiz.get("durations").get(0).get(1).asDouble();

            resultado.distanciaKm = distancia;
            resultado.tiempoMinutos = (int) Math.round(duracionSegundos / 60.0);
            resultado.exito = true;
            return resultado;

        } catch (Exception e) {
            resultado.exito = false;
            resultado.mensajeError = "No se pudo conectar con OpenRouteService: " + e.getMessage();
            return resultado;
        }
    }

    /**
     * Pide a ORS Directions la ruta REAL por carretera entre dos puntos:
     * la geometria completa (todos los puntos que forman la curva siguiendo
     * las calles) mas la distancia y duracion -- todo en una sola llamada.
     */
    public ResultadoRutaCompleta calcularRutaCompleta(double latOrigen, double lngOrigen,
                                                       double latDestino, double lngDestino) {
        ResultadoRutaCompleta resultado = new ResultadoRutaCompleta();
        try {
            String cuerpoJson = String.format(Locale.US,
                    "{\"coordinates\":[[%f,%f],[%f,%f]]}",
                    lngOrigen, latOrigen, lngDestino, latDestino);

            HttpClient cliente = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openrouteservice.org/v2/directions/driving-car/geojson"))
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(cuerpoJson))
                    .build();

            HttpResponse<String> respuesta = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() != 200) {
                resultado.exito = false;
                resultado.mensajeError = "Error ORS Directions (" + respuesta.statusCode() + "): " + respuesta.body();
                return resultado;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode raiz = mapper.readTree(respuesta.body());

            JsonNode feature = raiz.get("features").get(0);
            JsonNode resumen = feature.get("properties").get("summary");

            resultado.distanciaKm = resumen.get("distance").asDouble() / 1000.0; // metros -> km
            resultado.tiempoMinutos = (int) Math.round(resumen.get("duration").asDouble() / 60.0);

            // La geometria viene como [lng, lat] por cada punto -- la volteamos a [lat, lng]
            JsonNode coordenadas = feature.get("geometry").get("coordinates");
            for (JsonNode punto : coordenadas) {
                double lng = punto.get(0).asDouble();
                double lat = punto.get(1).asDouble();
                resultado.puntosRuta.add(new double[]{lat, lng});
            }

            resultado.exito = true;
            return resultado;

        } catch (Exception e) {
            resultado.exito = false;
            resultado.mensajeError = "No se pudo conectar con OpenRouteService: " + e.getMessage();
            return resultado;
        }
    }
}