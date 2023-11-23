package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.entity.User;
import com.example.seriesbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUsername_ReturnsUserOptional() {
        // Arrange
        String username = "john";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByEmail_ReturnsUserOptional() {
        // Arrange
        String email = "john@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void save_ReturnsSavedUser() {
        // Arrange
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.save(user);

        // Assert
        assertEquals(user, result);
    }

    @Test
    void doesUserExistByUsername_ReturnsTrueWhenUserExists() {
        // Arrange
        String username = "john";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = userService.doesUserExistByUsername(username);

        // Assert
        assertTrue(result);
    }

    @Test
    void doesUserExistByUsername_ReturnsFalseWhenUserDoesNotExist() {
        // Arrange
        String username = "john";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = userService.doesUserExistByUsername(username);

        // Assert
        assertFalse(result);
    }

    @Test
    void doesUserExistByEmail_ReturnsTrueWhenUserExists() {
        // Arrange
        String email = "john@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = userService.doesUserExistByEmail(email);

        // Assert
        assertTrue(result);
    }

    @Test
    void doesUserExistByEmail_ReturnsFalseWhenUserDoesNotExist() {
        // Arrange
        String email = "john@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = userService.doesUserExistByEmail(email);

        // Assert
        assertFalse(result);
    }
}
