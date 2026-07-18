package com.feedexx.encomiendas.repository;

import com.feedexx.encomiendas.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByCliente_IdCliente(Long idCliente);
    List<Envio> findByEstado(String estado);
}
