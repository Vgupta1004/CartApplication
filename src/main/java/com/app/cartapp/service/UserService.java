package com.app.cartapp.service;

import java.util.List;

import com.app.cartapp.dto.UserRequestDTO;
import com.app.cartapp.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO request);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO request);
}

