package com.dulceluna.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* Error 404: producto o categoría inexistente.*/
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> manejarRecursoNoEncontrado(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = crearError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                null);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /*Error 400: validaciones de los DTO. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> erroresCampos = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> erroresCampos.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()));

        ApiError error = crearError(
                HttpStatus.BAD_REQUEST,
                "Los datos enviados contienen errores de validación",
                request.getRequestURI(),
                erroresCampos);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /* Error 400: categoría repetida u otra operación inválida. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarArgumentoInvalido(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ApiError error = crearError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /* Error 400: JSON mal escrito o tipos de datos incorrectos.*/
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ApiError error = crearError(
                HttpStatus.BAD_REQUEST,
                "El cuerpo JSON está incompleto o contiene valores inválidos",
                request.getRequestURI(),
                null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /*Error 400: parámetro de URL con tipo incorrecto.*/
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipoInvalido(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        ApiError error = crearError(
                HttpStatus.BAD_REQUEST,
                "El parámetro '" + ex.getName() + "' tiene un valor inválido",
                request.getRequestURI(),
                null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /*Error 500: cualquier error inesperado.*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorGeneral(
            Exception ex,
            HttpServletRequest request) {

        ApiError error = crearError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno en el servidor",
                request.getRequestURI(),
                null);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    private ApiError crearError(
            HttpStatus estado,
            String mensaje,
            String ruta,
            Map<String, String> errores) {

        return new ApiError(
                LocalDateTime.now(),
                estado.value(),
                estado.getReasonPhrase(),
                mensaje,
                ruta,
                errores);
    }
}