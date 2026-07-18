package com.feedexx.encomiendas.repository;

import com.feedexx.encomiendas.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    // Util para el motor de grafos: obtener todas las aristas que salen de una ciudad
    List<Ruta> findByCiudadOrigen_IdCiudad(Long idCiudad);
}
