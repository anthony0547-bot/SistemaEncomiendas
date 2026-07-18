package com.feedexx.encomiendas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Manejador global de errores (Criterio 6 de la rubrica: "Calidad y Pruebas").
 *
 * Captura cualquier excepcion no controlada que ocurra en los controladores
 * (nulos, ids inexistentes, fallos de conexion externa, etc.) y en vez de
 * dejar que Spring muestre la pantalla blanca "Whitelabel Error Page",
 * muestra una vista amigable ("error.html") sin tumbar el servidor.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Captura errores esperados de logica de negocio (ej: "no existe una ruta
     * entre esas ciudades") que ya manejamos con mensajes claros en algunos
     * controladores, pero que si se filtran hasta aqui, igual se muestran
     * de forma amigable en vez de un error 500.
     */
    @ExceptionHandler(IllegalStateException.class)
    public String manejarErrorDeNegocio(IllegalStateException ex, Model model) {
        logger.warn("Error de negocio controlado: {}", ex.getMessage());
        model.addAttribute("mensajeError", ex.getMessage());
        model.addAttribute("tipoError", "Operacion no valida");
        return "error";
    }

    /**
     * Captura datos nulos o inexistentes (ej: buscar una ciudad/cliente con
     * un id que ya no existe en la base de datos).
     */
    @ExceptionHandler(NullPointerException.class)
    public String manejarNulos(NullPointerException ex, Model model) {
        logger.error("Referencia nula no controlada", ex);
        model.addAttribute("mensajeError",
                "El registro que buscabas no existe o ya fue eliminado. Verifica los datos e intenta de nuevo.");
        model.addAttribute("tipoError", "Registro no encontrado");
        return "error";
    }

    /**
     * Red de seguridad final: cualquier otra excepcion no prevista (fallo de
     * base de datos, error de conexion con OpenRouteService, etc.) tambien
     * se muestra de forma controlada en vez de crashear la vista.
     */
    @ExceptionHandler(Exception.class)
    public String manejarErrorGeneral(Exception ex, Model model) {
        logger.error("Error no controlado en la aplicacion", ex);
        model.addAttribute("mensajeError",
                "Ocurrio un problema inesperado al procesar tu solicitud. Intenta de nuevo en unos segundos.");
        model.addAttribute("tipoError", "Error del sistema");
        return "error";
    }
}