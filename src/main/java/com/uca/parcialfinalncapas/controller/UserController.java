package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.UserResponse;
import com.uca.parcialfinalncapas.security.CustomUserDetails;
import com.uca.parcialfinalncapas.service.UserService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getAllUsers() {
        List<UserResponse> users = userService.findAll();

        return ResponseBuilderUtil.buildResponse(
                "Usuarios obtenidos correctamente",
                users.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK,
                users
        );
    }

    @GetMapping("/techs")
    @PreAuthorize("hasRole('USER') or hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getAllTechs() {
        List<UserResponse> techs = userService.findByRole("TECH");
        return ResponseBuilderUtil.buildResponse(
                "Técnicos obtenidos correctamente",
                techs.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK,
                techs
        );
    }

    @GetMapping("/{correo}")
    @PreAuthorize("hasRole('TECH') or (hasRole('USER') and #correo == authentication.name)")
    public ResponseEntity<GeneralResponse> getUserByCorreo(@PathVariable String correo) {
        UserResponse user = userService.findByCorreo(correo);
        return ResponseBuilderUtil.buildResponse("Usuario encontrado", HttpStatus.OK, user);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getCurrentUser(Authentication authentication) {
        String correo = authentication.getName();
        UserResponse user = userService.findByCorreo(correo);
        return ResponseBuilderUtil.buildResponse("Perfil obtenido correctamente", HttpStatus.OK, user);
    }

    @PostMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> createUser(@Valid @RequestBody UserCreateRequest user) {
        UserResponse createdUser = userService.save(user);
        return ResponseBuilderUtil.buildResponse("Usuario creado correctamente", HttpStatus.CREATED, createdUser);
    }

    @PutMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> updateUser(@Valid @RequestBody UserUpdateRequest user) {
        UserResponse updatedUser = userService.update(user);
        return ResponseBuilderUtil.buildResponse("Usuario actualizado correctamente", HttpStatus.OK, updatedUser);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GeneralResponse> updateMyProfile(@Valid @RequestBody UserUpdateRequest user,
                                                           Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Asegurar que solo puede actualizar su propio perfil
        user.setId(userDetails.getUserId());

        UserResponse updatedUser = userService.update(user);
        return ResponseBuilderUtil.buildResponse("Perfil actualizado correctamente", HttpStatus.OK, updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseBuilderUtil.buildResponse("Usuario eliminado correctamente", HttpStatus.OK, null);
    }
}