package com.uca.parcialfinalncapas.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    /**
     * Genera un token JWT para el usuario dado
     */
    String generateToken(UserDetails userDetails);

    /**
     * Extrae el nombre de usuario del token
     */
    String extractUsername(String token);

    /**
     * Valida si el token es válido para el usuario dado
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Verifica si el token ha expirado
     */
    boolean isTokenExpired(String token);
}