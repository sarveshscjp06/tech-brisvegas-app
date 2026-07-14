package com.brisvegastech.registration.service;

import com.brisvegastech.registration.dto.RegisterRequest;
import com.brisvegastech.registration.entity.UserEntity;
import com.brisvegastech.registration.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegisterRequest request) {
        // 1. Check if the username is already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already registered!");
        }

        // 2. Map DTO to Entity and hash the plaintext password
        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Crucial security step
        
        // Assign default 'USER' role if none are provided
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            newUser.setRoles(Set.of("USER"));
        } else {
            newUser.setRoles(request.getRoles());
        }

        // 3. Save to database
        userRepository.save(newUser);
        return "User registered successfully!";
    }
}