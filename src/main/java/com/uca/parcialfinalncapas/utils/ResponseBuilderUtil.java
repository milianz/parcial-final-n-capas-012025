package com.uca.parcialfinalncapas.utils;

import com.uca.parcialfinalncapas.dto.response.ErrorResponse;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;

// Clase para construir respuestas de error y éxito de manera uniforme
public class ResponseBuilderUtil {

    // Método para construir una respuesta de error
    public static ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(data)
                .status(status.value())
                .time(LocalDate.now())
                .uri(uri)
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

    // Método para construir una respuesta general de éxito
    public static ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath();

        GeneralResponse generalResponse = GeneralResponse.builder()
                .message(message)
                .status(status.value())
                .data(data)
                .uri(uri)
                .time(LocalDate.now())
                .build();

        return ResponseEntity.status(status).body(generalResponse);
    }
}