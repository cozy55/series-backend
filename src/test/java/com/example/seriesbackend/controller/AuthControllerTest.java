package com.example.seriesbackend.controller;

import com.example.seriesbackend.dto.JWTAuthResponse;
import com.example.seriesbackend.dto.LoginDto;
import com.example.seriesbackend.dto.RegistrationDto;
import com.example.seriesbackend.service.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ReturnsJWTAuthResponse() {
        // Arrange
        LoginDto loginDto = new LoginDto("", "");
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse("jwt-token");
        when(authService.login(loginDto)).thenReturn("jwt-token");

        // Act
        ResponseEntity<JWTAuthResponse> result = authController.authenticate(loginDto);

        // Assert
        assertEquals(jwtAuthResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authService, times(1)).login(loginDto);
    }

    @Test
    void register_ReturnsOkHttpStatus() {
        // Arrange
        RegistrationDto registrationDto = new RegistrationDto("", "", "", "");

        // Act
        ResponseEntity<Void> result = authController.register(registrationDto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authService, times(1)).register(registrationDto);
    }

    @Test
    void confirm_ReturnsJWTAuthResponse() {
        // Arrange
        String token = "confirmation-token";
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse("jwt-token");
        when(authService.confirmUserAccount(token)).thenReturn("jwt-token");

        // Act
        ResponseEntity<JWTAuthResponse> result = authController.confirm(token);

        // Assert
        assertEquals(jwtAuthResponse, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(authService, times(1)).confirmUserAccount(token);
    }
}
