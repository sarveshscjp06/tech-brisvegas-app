package com.brisvegastech.registration.controller;

import com.brisvegastech.registration.dto.LoginRequest;
import com.brisvegastech.registration.dto.AuthenticationResponse;
import com.brisvegastech.registration.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthenticationController(
            AuthenticationManager authenticationManager, 
            UserDetailsService userDetailsService, 
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        
        // 1. Hand over raw credentials to Spring Security. 
        // Behind the scenes, DaoAuthenticationProvider pulls the user from the DB,
        // hashes incoming pass, and throws BadCredentialsException if they don't match.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. If no exception was thrown, the user is valid. Fetch full user details.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        
        // 3. Generate a stateless JWT token packed with claims and roles.
        final String jwtToken = jwtService.generateToken(userDetails);

        // 4. Return structured JSON payload containing the token to the client.
        return ResponseEntity.ok(new AuthenticationResponse(
                jwtToken,
                "Bearer",
                3600000 // 1 hour in milliseconds
        ));
    }
}