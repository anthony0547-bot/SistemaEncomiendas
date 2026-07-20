package com.feedexx.encomiendas.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Long idEnvio;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @Column(name = "costo", nullable = false)
    private Double costo;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; // REGISTRADO, EN_TRANSITO, ENTREGADO

    @Column(name = "dimensiones", nullable = false)
    private String dimensiones = "Estándar";

    // MAPEO CORRECTO HACIA LA TABLA USUARIOS (Y SU COLUMNA id_usuario)
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_repartidor", referencedColumnName = "id_repartidor")
    private Repartidor repartidor;

    @ManyToOne
    @JoinColumn(name = "id_ruta", referencedColumnName = "id_ruta", nullable = false)
    private Ruta ruta;

    public Envio() {
        this.dimensiones = "Estándar";
    }

    public Envio(LocalDate fecha, Double peso, Double costo, String estado, String dimensiones,
                 Usuario usuario, Repartidor repartidor, Ruta ruta) {

        this.fecha = fecha;
        this.peso = peso;
        this.costo = costo;
        this.estado = estado;
        this.dimensiones = (dimensiones != null && !dimensiones.isBlank()) ? dimensiones : "Estándar";
        this.usuario = usuario;
        this.repartidor = repartidor;
        this.ruta = ruta;
    }

    public Long getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Long idEnvio) {
        this.idEnvio = idEnvio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        if (dimensiones == null || dimensiones.isBlank()) {
            this.dimensiones = "Estándar";
        } else {
            this.dimensiones = dimensiones;
        }
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}