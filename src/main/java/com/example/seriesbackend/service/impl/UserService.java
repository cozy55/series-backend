package com.example.seriesbackend.service.impl;

import com.example.seriesbackend.entity.User;
import com.example.seriesbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean doesUserExistByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean doesUserExistByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
