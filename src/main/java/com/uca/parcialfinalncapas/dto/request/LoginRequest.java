package com.uca.parcialfinalncapas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}