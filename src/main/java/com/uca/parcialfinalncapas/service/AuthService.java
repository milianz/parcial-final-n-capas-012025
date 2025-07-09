package com.uca.parcialfinalncapas.service;

import com.uca.parcialfinalncapas.dto.request.LoginRequest;
import com.uca.parcialfinalncapas.dto.request.RegisterRequest;
import com.uca.parcialfinalncapas.dto.response.AuthResponse;

public interface AuthService {
    /**
     * Registra un nuevo usuario
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Autentica un usuario y retorna un token JWT
     */
    AuthResponse login(LoginRequest request);
}