package com.feedexx.encomiendas.interceptor;

import com.feedexx.encomiendas.entity.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Restringe el acceso a las pantallas de gestion (Ciudades, Rutas, Usuarios,
 * Repartidores, Comprobantes) solo a usuarios con rol ADMIN.
 *
 * Un cliente comun jamas deberia poder registrar ciudades, rutas o ver la
 * gestion interna -- solo elige de listas ya cargadas en /envios.
 */
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioActual") == null) {
            // No ha iniciado sesion: lo mandamos al login
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");

        if (!"ADMIN".equals(usuarioActual.getRol())) {
            // Inicio sesion, pero no es administrador: acceso denegado
            response.sendRedirect(request.getContextPath() + "/?accesoDenegado=true");
            return false;
        }

        return true; // Es ADMIN, puede pasar
    }
}