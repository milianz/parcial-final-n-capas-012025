package com.uca.parcialfinalncapas.security;

import com.uca.parcialfinalncapas.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación personalizada de UserDetails que envuelve la entidad User.
 * Esta clase es utilizada por Spring Security para manejar la autenticación y autorización.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Retorna las autoridades (roles) del usuario.
     * Convierte el rol del usuario en una autoridad de Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getNombreRol()));
    }

    /**
     * Retorna la contraseña del usuario.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Retorna el nombre de usuario (en este caso, el correo).
     */
    @Override
    public String getUsername() {
        return user.getCorreo();
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario no han expirado.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Método auxiliar para obtener la entidad User original.
     */
    public User getUser() {
        return user;
    }

    /**
     * Método auxiliar para obtener el ID del usuario.
     */
    public Long getUserId() {
        return user.getId();
    }

    /**
     * Método auxiliar para obtener el rol del usuario.
     */
    public String getRole() {
        return user.getNombreRol();
    }
}