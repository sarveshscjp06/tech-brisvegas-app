package com.brisvegastech.registration.controller;

import com.brisvegastech.registration.dto.RegisterRequest;
import com.brisvegastech.registration.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        try {
            String message = userService.registerUser(request);
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Handled natively via basic auth interceptors
    @GetMapping("/login")
    public ResponseEntity<?> login(@AuthenticationPrincipal UserDetails userDetails) {
        // If the execution flow reaches this point, authentication was successful!
        return ResponseEntity.ok(Map.of(
            "message", "Login successful!",
            "username", userDetails.getUsername(),
            "roles", userDetails.getAuthorities()
        ));
    }
}