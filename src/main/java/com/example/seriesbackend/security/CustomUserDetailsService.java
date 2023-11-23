package com.example.seriesbackend.security;

import com.example.seriesbackend.entity.User;
import com.example.seriesbackend.exception.RegistrationException;
import com.example.seriesbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

        if(!user.isEnabled()){
            throw new RegistrationException("Error: User has not activated an account!");
        }

        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,
                user.getPassword(),
                Set.of()
        );
    }
}
