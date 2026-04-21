package com.app.cartapp.testservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.app.cartapp.dto.UserRequestDTO;
import com.app.cartapp.dto.UserResponseDTO;
import com.app.cartapp.exception.ResourceNotFoundException;
import com.app.cartapp.model.User;
import com.app.cartapp.model.UserType;
import com.app.cartapp.repository.UserRepository;
import com.app.cartapp.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPhone("1234567890");
        user.setUserType(UserType.CUSTOMER);

        request = new UserRequestDTO();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPhone("1234567890");
        request.setUserType(UserType.CUSTOMER);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("john@example.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserResponseDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.updateUser(1L, request);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(1L, request);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }
}