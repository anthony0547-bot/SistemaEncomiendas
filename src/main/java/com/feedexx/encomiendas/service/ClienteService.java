package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Cliente;
import com.feedexx.encomiendas.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    /**
     * Busca el Cliente asociado a un Usuario logueado (por id_usuario).
     * Se usa para tomar el cliente automaticamente de la sesion al registrar
     * un envio, en vez de pedirselo en un select.
     */
    public Cliente buscarPorUsuario(Long idUsuario) {
        return clienteRepository.findByUsuario_IdUsuario(idUsuario);
    }
}