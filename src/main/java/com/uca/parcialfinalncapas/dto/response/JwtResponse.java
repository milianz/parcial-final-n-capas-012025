package com.uca.parcialfinalncapas.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JwtResponse {
    private String token;
    private String tipo;
    private LocalDateTime expiracion;
    private String usuario;
    private String rol;
}