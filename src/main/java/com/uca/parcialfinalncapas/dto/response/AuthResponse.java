package com.uca.parcialfinalncapas.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String tipo; // "Bearer"
    private Long id;
    private String correo;
    private String nombre;
    private String rol;
}