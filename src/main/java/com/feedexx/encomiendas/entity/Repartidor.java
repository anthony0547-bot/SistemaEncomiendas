package com.feedexx.encomiendas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "repartidores")
public class Repartidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repartidor")
    private Long idRepartidor;

    @Column(name = "licencia", nullable = false, length = 20)
    private String licencia;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // DISPONIBLE, EN_RUTA, INACTIVO

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    public Repartidor() {
    }

    public Repartidor(String licencia, String telefono, String estado, Usuario usuario) {
        this.licencia = licencia;
        this.telefono = telefono;
        this.estado = estado;
        this.usuario = usuario;
    }

    public Long getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(Long idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
