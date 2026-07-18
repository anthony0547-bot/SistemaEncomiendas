package com.feedexx.encomiendas.service;

import com.feedexx.encomiendas.entity.Usuario;
import com.feedexx.encomiendas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    /**
     * Valida las credenciales de login (usadas por el correo, a modo de "ID de usuario").
     * Devuelve el Usuario si coinciden, o null si no existe o la contrasena no coincide.
     */
    public Usuario autenticar(String correo, String contrasena) {
        return usuarioRepository.findByCorreoAndContrasena(correo, contrasena).orElse(null);
    }
}
