package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Repartidor;
import com.feedexx.encomiendas.repository.RepartidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;

    public List<Repartidor> listarTodos() {
        return repartidorRepository.findAll();
    }

    public List<Repartidor> listarDisponibles() {
        return repartidorRepository.findByEstado("DISPONIBLE");
    }

    public Repartidor guardar(Repartidor repartidor) {
        return repartidorRepository.save(repartidor);
    }

    public Repartidor buscarPorId(Long id) {
        return repartidorRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repartidorRepository.deleteById(id);
    }
}
