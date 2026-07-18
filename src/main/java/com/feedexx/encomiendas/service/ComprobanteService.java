package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Comprobante;
import com.feedexx.encomiendas.repository.ComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComprobanteService {

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    public List<Comprobante> listarTodos() {
        return comprobanteRepository.findAll();
    }

    public Comprobante guardar(Comprobante comprobante) {
        return comprobanteRepository.save(comprobante);
    }

    public Comprobante buscarPorId(Long id) {
        return comprobanteRepository.findById(id).orElse(null);
    }
}
