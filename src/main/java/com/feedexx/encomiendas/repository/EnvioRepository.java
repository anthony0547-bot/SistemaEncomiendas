package com.feedexx.encomiendas.repository;

import com.feedexx.encomiendas.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    List<Envio> findByUsuario_IdUsuario(Long idUsuario);

    List<Envio> findByEstado(String estado);
    List<Envio> findByRepartidor_IdRepartidor(Long idRepartidor);

}
