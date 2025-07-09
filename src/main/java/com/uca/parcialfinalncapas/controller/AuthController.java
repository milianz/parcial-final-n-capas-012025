package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.LoginRequest;
import com.uca.parcialfinalncapas.dto.request.RegisterRequest;
import com.uca.parcialfinalncapas.dto.response.AuthResponse;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.service.AuthService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        try {
            return ResponseBuilderUtil.buildResponse(
                    "Usuario registrado exitosamente",
                    HttpStatus.CREATED,
                    response
            );
        } catch (Exception e) {
            return ResponseBuilderUtil.buildResponse(
                    "Error al registrar el usuario: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        try {
            return ResponseBuilderUtil.buildResponse(
                    "Login exitoso",
                    HttpStatus.OK,
                    response
            );
        } catch (Exception e) {
            return ResponseBuilderUtil.buildResponse(
                    "Error al iniciar sesión: " + e.getMessage(),
                    HttpStatus.UNAUTHORIZED,
                    null
            );
        }
    }

    @GetMapping("/test")
    public ResponseEntity<GeneralResponse> test() {
        try {
            return ResponseBuilderUtil.buildResponse(
                    "Endpoint de prueba funcionando",
                    HttpStatus.OK,
                    "¡La autenticación está funcionando!"
            );
        } catch (Exception e) {
            return ResponseBuilderUtil.buildResponse(
                    "Error en el endpoint de prueba: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }
}