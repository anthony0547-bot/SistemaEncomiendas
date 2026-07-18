package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Ruta;
import com.feedexx.encomiendas.repository.RutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RutaService {

    @Autowired
    private RutaRepository rutaRepository;

    public List<Ruta> listarTodas() {
        return rutaRepository.findAll();
    }

    public Ruta guardar(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public Ruta buscarPorId(Long id) {
        return rutaRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        rutaRepository.deleteById(id);
    }
}
