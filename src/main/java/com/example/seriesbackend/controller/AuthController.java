package com.example.seriesbackend.controller;

import com.example.seriesbackend.dto.JWTAuthResponse;
import com.example.seriesbackend.dto.LoginDto;
import com.example.seriesbackend.dto.RegistrationDto;
import com.example.seriesbackend.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public @ResponseBody ResponseEntity<JWTAuthResponse> authenticate(@RequestBody LoginDto loginDto){
        return new ResponseEntity<>(new JWTAuthResponse(authService.login(loginDto)), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> register(@RequestBody RegistrationDto registrationDto){
        authService.register(registrationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<JWTAuthResponse> confirm(@RequestParam String token){
        return new ResponseEntity<>(new JWTAuthResponse(authService.confirmUserAccount(token)), HttpStatus.OK);
    }
}
