package com.uca.parcialfinalncapas.service.impl;

import com.uca.parcialfinalncapas.dto.request.LoginRequest;
import com.uca.parcialfinalncapas.dto.request.RegisterRequest;
import com.uca.parcialfinalncapas.dto.response.AuthResponse;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.exceptions.UserNotFoundException;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.security.CustomUserDetails;
import com.uca.parcialfinalncapas.service.AuthService;
import com.uca.parcialfinalncapas.service.JwtService;
import com.uca.parcialfinalncapas.utils.enums.Rol;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new UserNotFoundException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        // Validar que el rol sea válido
        if (!request.getNombreRol().equals(Rol.USER.getValue()) &&
                !request.getNombreRol().equals(Rol.TECH.getValue())) {
            throw new IllegalArgumentException("Rol inválido. Debe ser USER o TECH");
        }

        // Crear nuevo usuario
        var user = User.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombreRol(request.getNombreRol())
                .build();

        userRepository.save(user);

        // Generar token
        var jwtToken = jwtService.generateToken(new CustomUserDetails(user));

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .id(user.getId())
                .correo(user.getCorreo())
                .nombre(user.getNombre())
                .rol(user.getNombreRol())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getPassword()
                )
        );

        // Buscar usuario en la base de datos
        var user = userRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Generar token
        var jwtToken = jwtService.generateToken(new CustomUserDetails(user));

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .id(user.getId())
                .correo(user.getCorreo())
                .nombre(user.getNombre())
                .rol(user.getNombreRol())
                .build();
    }
}