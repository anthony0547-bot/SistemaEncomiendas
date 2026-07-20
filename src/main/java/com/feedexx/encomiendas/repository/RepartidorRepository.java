package com.feedexx.encomiendas.repository;

import com.feedexx.encomiendas.entity.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;



public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
    List<Repartidor> findByEstado(String estado);
    Optional<Repartidor> findByUsuario_IdUsuario(Long idUsuario);
}

