package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.dto.LoginDto;
import com.example.seriesbackend.dto.RegistrationDto;
import com.example.seriesbackend.entity.ConfirmationToken;
import com.example.seriesbackend.entity.User;
import com.example.seriesbackend.exception.RegistrationException;
import com.example.seriesbackend.repository.ConfirmationTokenRepository;
import com.example.seriesbackend.repository.UserRepository;
import com.example.seriesbackend.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static int DAYS_TO_EXPIRE = 1;

    public String login(LoginDto loginDto){
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public void register(RegistrationDto registrationDto){
        if(userService.doesUserExistByUsername(registrationDto.getUsername())){
            throw new RegistrationException("Error: Username is already in use!");
        }
        if(userService.doesUserExistByEmail(registrationDto.getEmail())){
            throw new RegistrationException("Error: Email is already in use!");
        }
        if(!registrationDto.getPassword().equals(registrationDto.getMatchingPassword())){
            throw new RegistrationException("Error: Passwords do not match!");
        }

        var u = new User(registrationDto.getUsername(), registrationDto.getEmail(), bCryptPasswordEncoder.encode(registrationDto.getPassword()));
        var user = userService.save(u);
        var confirmationToken = confirmationTokenRepository.save(new ConfirmationToken(user));

        var text = String.format("To confirm your account, please click here: http://localhost:8080/api/auth/confirm-account?token=%s", confirmationToken.getToken());
        mailService.send(user.getEmail(), "Complete registration!", text);

        System.out.println("Confirmation Token: " + confirmationToken.getToken());
    }

    public String confirmUserAccount(String token){
        var confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RegistrationException("Error: Activation token is not found!"));

        if(confirmationToken.getCreatedDate().toInstant().plus(DAYS_TO_EXPIRE, ChronoUnit.DAYS).isBefore(Instant.now())) {
            throw new RegistrationException("Error: Activation token has expired!");
        }

        var user = confirmationToken.getUser();
        user.setEnabled(true);
        user = userService.save(user);

        return login(new LoginDto(user.getEmail(), user.getPassword()));
    }
}
