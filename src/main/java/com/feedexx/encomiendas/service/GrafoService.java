package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Ciudad;
import com.feedexx.encomiendas.entity.Ruta;
import com.feedexx.encomiendas.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Motor de grafos del sistema de encomiendas.
 *
 * Modela las Ciudades como nodos y las Rutas como aristas con peso
 * (distancia_km), usando una lista de adyacencia dinamica (no una matriz
 * de adyacencia, que desperdiciaria memoria si hay muchas ciudades sin
 * conexion directa entre si).
 *
 * Implementa Dijkstra para encontrar el camino mas corto entre dos ciudades.
 */
@Service
public class GrafoService {

    @Autowired
    private RutaRepository rutaRepository;

    /**
     * Representa una arista dentro de la lista de adyacencia:
     * ciudad destino + peso (distancia en km) + la Ruta original (para
     * poder recuperar tiempoEstimado, etc.).
     */
    public static class Arista {
        public Long idCiudadDestino;
        public double distanciaKm;
        public Ruta rutaOrigen;

        public Arista(Long idCiudadDestino, double distanciaKm, Ruta rutaOrigen) {
            this.idCiudadDestino = idCiudadDestino;
            this.distanciaKm = distanciaKm;
            this.rutaOrigen = rutaOrigen;
        }
    }

    /**
     * Resultado del algoritmo: la secuencia de ciudades del camino optimo,
     * la distancia total acumulada y la lista de rutas (aristas) usadas.
     */
    public static class ResultadoRuta {
        public List<Long> caminoCiudades = new ArrayList<>();
        public List<Ruta> rutasUsadas = new ArrayList<>();
        public double distanciaTotal;
        public boolean existeCamino;
    }

    /**
     * Construye la lista de adyacencia dinamica del grafo completo a partir
     * de todas las Rutas registradas en la base de datos.
     * Las rutas se consideran bidireccionales (si existe A->B tambien se
     * puede recorrer B->A con el mismo peso), lo cual es habitual en un
     * mapa de carreteras.
     */
    public Map<Long, List<Arista>> construirListaAdyacencia() {
        Map<Long, List<Arista>> listaAdyacencia = new HashMap<>();
        List<Ruta> rutas = rutaRepository.findAll();

        for (Ruta ruta : rutas) {
            Long idOrigen = ruta.getCiudadOrigen().getIdCiudad();
            Long idDestino = ruta.getCiudadDestino().getIdCiudad();
            double distancia = ruta.getDistanciaKm();

            listaAdyacencia.computeIfAbsent(idOrigen, k -> new ArrayList<>())
                    .add(new Arista(idDestino, distancia, ruta));

            listaAdyacencia.computeIfAbsent(idDestino, k -> new ArrayList<>())
                    .add(new Arista(idOrigen, distancia, ruta));
        }
        return listaAdyacencia;
    }

    /**
     * Algoritmo de Dijkstra clasico usando una cola de prioridad (PriorityQueue),
     * lo que da complejidad O((V + E) log V).
     */
    public ResultadoRuta calcularCaminoMasCorto(Long idCiudadOrigen, Long idCiudadDestino) {
        Map<Long, List<Arista>> grafo = construirListaAdyacencia();

        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, Long> predecesores = new HashMap<>();
        Map<Long, Ruta> rutaUsadaHaciaNodo = new HashMap<>();
        Set<Long> visitados = new HashSet<>();

        PriorityQueue<Long> cola = new PriorityQueue<>(Comparator.comparingDouble(
                nodo -> distancias.getOrDefault(nodo, Double.MAX_VALUE)));

        distancias.put(idCiudadOrigen, 0.0);
        cola.add(idCiudadOrigen);

        while (!cola.isEmpty()) {
            Long actual = cola.poll();

            if (visitados.contains(actual)) {
                continue;
            }
            visitados.add(actual);

            if (actual.equals(idCiudadDestino)) {
                break;
            }

            List<Arista> vecinos = grafo.getOrDefault(actual, Collections.emptyList());
            for (Arista arista : vecinos) {
                if (visitados.contains(arista.idCiudadDestino)) {
                    continue;
                }
                double nuevaDistancia = distancias.get(actual) + arista.distanciaKm;
                double distanciaConocida = distancias.getOrDefault(arista.idCiudadDestino, Double.MAX_VALUE);

                if (nuevaDistancia < distanciaConocida) {
                    distancias.put(arista.idCiudadDestino, nuevaDistancia);
                    predecesores.put(arista.idCiudadDestino, actual);
                    rutaUsadaHaciaNodo.put(arista.idCiudadDestino, arista.rutaOrigen);
                    cola.add(arista.idCiudadDestino);
                }
            }
        }

        ResultadoRuta resultado = new ResultadoRuta();

        if (!distancias.containsKey(idCiudadDestino)) {
            resultado.existeCamino = false;
            return resultado;
        }

        // Reconstruir el camino desde el destino hacia el origen usando los predecesores
        LinkedList<Long> camino = new LinkedList<>();
        LinkedList<Ruta> rutas = new LinkedList<>();
        Long paso = idCiudadDestino;

        while (paso != null && !paso.equals(idCiudadOrigen)) {
            camino.addFirst(paso);
            rutas.addFirst(rutaUsadaHaciaNodo.get(paso));
            paso = predecesores.get(paso);
        }
        camino.addFirst(idCiudadOrigen);

        resultado.caminoCiudades = camino;
        resultado.rutasUsadas = rutas;
        resultado.distanciaTotal = distancias.get(idCiudadDestino);
        resultado.existeCamino = true;

        return resultado;
    }
}
