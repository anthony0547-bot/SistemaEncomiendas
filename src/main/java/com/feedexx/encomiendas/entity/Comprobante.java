package com.feedexx.encomiendas.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "comprobantes")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Long idComprobante;

    @Column(name = "numero", nullable = false, unique = true, length = 30)
    private String numero;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "distancia", nullable = false)
    private Double distancia;

    // FK necesaria para saber a que envio pertenece el comprobante (1 a 1)
    @OneToOne
    @JoinColumn(name = "id_envio", referencedColumnName = "id_envio", nullable = false, unique = true)
    private Envio envio;

    public Comprobante() {
    }

    public Comprobante(String numero, LocalDate fecha, Double distancia, Envio envio) {
        this.numero = numero;
        this.fecha = fecha;
        this.distancia = distancia;
        this.envio = envio;
    }

    public Long getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(Long idComprobante) {
        this.idComprobante = idComprobante;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }
}
