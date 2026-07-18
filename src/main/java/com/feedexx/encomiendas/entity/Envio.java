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

    @Column(name = "dimensiones", nullable = false, length = 50)
    private String dimensiones;

    @Column(name = "costo", nullable = false)
    private Double costo;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; // REGISTRADO, EN_TRANSITO, ENTREGADO

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_repartidor", referencedColumnName = "id_repartidor")
    private Repartidor repartidor;

    @ManyToOne
    @JoinColumn(name = "id_ruta", referencedColumnName = "id_ruta", nullable = false)
    private Ruta ruta;

    public Envio() {
    }

    public Envio(LocalDate fecha, Double peso, String dimensiones, Double costo, String estado,
                 Cliente cliente, Repartidor repartidor, Ruta ruta) {
        this.fecha = fecha;
        this.peso = peso;
        this.dimensiones = dimensiones;
        this.costo = costo;
        this.estado = estado;
        this.cliente = cliente;
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

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
}
