package com.feedexx.encomiendas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long idRuta;

    // Estas dos relaciones representan las "aristas" del grafo: cada Ruta conecta
    // dos nodos (Ciudad origen y Ciudad destino) con un peso (distancia_km).
    @ManyToOne
    @JoinColumn(name = "id_ciudad_origen", referencedColumnName = "id_ciudad", nullable = false)
    private Ciudad ciudadOrigen;

    @ManyToOne
    @JoinColumn(name = "id_ciudad_destino", referencedColumnName = "id_ciudad", nullable = false)
    private Ciudad ciudadDestino;

    @Column(name = "distancia_km", nullable = false)
    private Double distanciaKm;

    @Column(name = "tiempo_estimado", nullable = false)
    private Integer tiempoEstimado; // minutos
    
    @Column(name = "costo_envio", nullable =false)
    private Double costoEnvio;

    public Ruta() {
    }

    public Ruta(Ciudad ciudadOrigen, Ciudad ciudadDestino, Double distanciaKm, Integer tiempoEstimado,Double costoEnvio) {
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.distanciaKm = distanciaKm;
        this.tiempoEstimado = tiempoEstimado;
        this.costoEnvio = costoEnvio;
    }

    public Long getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }

    public Ciudad getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(Ciudad ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }

    public Ciudad getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(Ciudad ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Integer getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Integer tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }
    public Double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }
}
