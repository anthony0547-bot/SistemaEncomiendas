package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Ciudad;
import com.feedexx.encomiendas.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    public List<Ciudad> listarTodas() {
        return ciudadRepository.findAll();
    }

    public Ciudad guardar(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    public Ciudad buscarPorId(Long id) {
        return ciudadRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        ciudadRepository.deleteById(id);
    }
}
