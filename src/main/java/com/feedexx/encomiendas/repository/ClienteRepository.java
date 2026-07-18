package com.feedexx.encomiendas.repository;

import com.feedexx.encomiendas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByCedula(String cedula);
    Cliente findByUsuario_IdUsuario(Long idUsuario);
}