package com.uca.parcialfinalncapas.service;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo el correo electrónico del usuario a buscar
     * @return un objeto UserResponse si se encuentra, o null si no existe
     */
    UserResponse findByCorreo(String correo);

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param user el objeto UserCreateRequest a guardar
     * @return el usuario guardado
     */
    UserResponse save(UserCreateRequest user);

    /**
     * Actualiza un usuario existente.
     *
     * @param user el objeto UserUpdateRequest con los datos actualizados
     * @return el usuario actualizado
     */
    UserResponse update(UserUpdateRequest user);

    /**
     * Elimina un usuario por su ID.
     *
     * @param id el ID del usuario a eliminar
     */
    void delete(Long id);

    /**
     * Buscar todos los usuarios.
     *
     * @return lista de todos los usuarios
     */
    List<UserResponse> findAll();

    /**
     * Busca usuarios por rol.
     *
     * @param role el rol a buscar (USER o TECH)
     * @return lista de usuarios con ese rol
     */
    List<UserResponse> findByRole(String role);

    /**
     * Verifica si un usuario es el usuario actual.
     *
     * @param userId el ID del usuario
     * @param currentUserEmail el correo del usuario actual
     * @return true si es el mismo usuario, false si no
     */
    boolean isCurrentUser(Long userId, String currentUserEmail);
}