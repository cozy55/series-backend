package com.example.seriesbackend.dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
    String accessToken;
    String tokenType = "Bearer";

    public JWTAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
