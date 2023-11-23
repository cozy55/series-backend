package com.example.seriesbackend.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.seriesbackend.dto.LoginDto;
import com.example.seriesbackend.dto.RegistrationDto;
import com.example.seriesbackend.entity.ConfirmationToken;
import com.example.seriesbackend.entity.User;
import com.example.seriesbackend.exception.RegistrationException;
import com.example.seriesbackend.repository.ConfirmationTokenRepository;
import com.example.seriesbackend.repository.UserRepository;
import com.example.seriesbackend.security.JwtTokenProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    private MailService mailService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ValidLoginDto_ReturnsToken() {
        // Arrange
        LoginDto loginDto = new LoginDto("username", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("token");

        // Act
        String token = authService.login(loginDto);

        // Assert
        assertEquals("token", token);
    }

    @Test
    void register_ValidRegistrationDto_RegistrationSuccessful() {
        // Arrange
        RegistrationDto registrationDto = new RegistrationDto("username", "email@example.com", "password", "password");
        User user = new User("username", "email@example.com", "encoded_password");
        when(userService.doesUserExistByUsername("username")).thenReturn(false);
        when(userService.doesUserExistByEmail("email@example.com")).thenReturn(false);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded_password");
        when(userService.save(any(User.class))).thenReturn(user);
        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(new ConfirmationToken(user));

        // Act
        authService.register(registrationDto);

        // Assert
        // Add your assertions here
    }

    @Test
    void confirmUserAccount_ValidToken_ReturnsToken() {
        // Arrange
        String token = "valid_token";
        User user = new User("username", "email@example.com", "encoded_password");
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationToken.setCreatedDate(Date.from(Instant.now()));
        when(confirmationTokenRepository.findByToken(token)).thenReturn(Optional.of(confirmationToken));
        when(userService.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("token");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("token");
        // Act
        String resultToken = authService.confirmUserAccount(token);

        // Assert
        assertEquals("token", resultToken);
    }

}
