package com.example.seriesbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}
