package com.example.seriesbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDto {
    @NotNull
    @NotEmpty
    String username;

    @NotNull
    @NotEmpty
    String email;

    @NotNull
    @NotEmpty
    String password;

    @NotNull
    @NotEmpty
    String matchingPassword;
}
