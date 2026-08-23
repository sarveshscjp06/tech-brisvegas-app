package com.brisvegastech.service;

import com.brisvegastech.dto.EnrollRequest;
import com.brisvegastech.entity.UserEntity;
import com.brisvegastech.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String enrollUser(EnrollRequest request) {
        // 1. Check if the username is already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already enrolled!");
        }

        // 2. Map DTO to Entity and hash the plaintext password
        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Crucial security step
        newUser.setEmail(request.getEmail());
        newUser.setMobile(request.getMobile());
        
        // Assign default 'USER' role if none are provided
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            newUser.setRoles(Set.of("GUEST_USER"));
        } else {
            newUser.setRoles(Set.of(request.getRoles().toString()));
        }
        
        // Assign default 'ACTIVE' status if none are provided
        if (request.getStatus()!= null || !request.getStatus().isEmpty()) {
            newUser.setStatus(request.getStatus().toString());
        }

        // 3. Save to database
        userRepository.save(newUser);
        return "User enrolled successfully!";
    }

    @Transactional
    public boolean updateUserForEmailVerification(String userName, String email) {
        UserEntity entity = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NullPointerException("data not found"));

        if (!entity.getEmail().equalsIgnoreCase(email)) {
            throw new NullPointerException("email not found");
        }
        entity.setEmailVerified(1);
        UserEntity updated = userRepository.save(entity);
        return true;
    }

    @Transactional
    public boolean updateUserForMobileVerification(String userName, String email, Long mobile) {
        UserEntity entity = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NullPointerException("data not found"));

        if (!entity.getEmail().equalsIgnoreCase(email)) {
            throw new NullPointerException("email not found");
        }
        entity.setMobileVerified(1);
        UserEntity updated = userRepository.save(entity);
        return true;
    }
}