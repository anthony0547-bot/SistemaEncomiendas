package com.feedexx.encomiendas.repository;

import com.feedexx.encomiendas.entity.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
}
