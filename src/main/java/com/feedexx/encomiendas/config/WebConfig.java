package com.feedexx.encomiendas.config;

import com.feedexx.encomiendas.interceptor.AdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registra el AdminInterceptor sobre las rutas de gestion interna que solo
 * un administrador debe poder usar. Las rutas publicas (/, /envios, /rastreo,
 * /login, /grafo) quedan libres para cualquier persona.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns(
                        "/ciudades/**",
                        "/rutas/**",
                        /**"/usuarios/**",*/
                        "/repartidores/**",
                        "/comprobantes/**"
                );
    }
}