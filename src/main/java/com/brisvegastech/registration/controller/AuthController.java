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

    /**
     * How to set the Type field configuration option to Basic Auth when HTTP request come from frontend and not through postmanWhen
     *  a request comes from a frontend application (like React, Angular, Vue, or vanilla JavaScript) instead of Postman, you cannot use a graphical "Type" dropdown. 
     * Instead, you must manually construct and inject the standard Authorization header into your HTTP request payload.
     * HTTP Basic Authentication requires you to format the credentials as a Base64-encoded string: Basic base64(username:password).
     * Here is how to implement this across different frontend environments.1. Using modern JavaScript (fetch API)If you are using native browser APIs, 
     * you must convert the credentials using the built-in btoa() (binary-to-ASCII) function to handle the Base64 encoding.javascriptconst 
     * username = 'alice_smith';
const password = 'mypassword123';

// 1. Combine and encode credentials into a Base64 token
const token = btoa(`${username}:${password}`);

// 2. Fire the network request with the manual Authorization header
fetch('http://localhost:8080/api/auth/login', {
    method: 'GET',
    headers: {
        'Authorization': `Basic ${token}`,
        'Content-Type': 'application/json'
    }
})
.then(response => {
    if (!response.ok) throw new Error('Unauthorized');
    return response.json();
})
.then(data => console.log('Login Success:', data))
.catch(error => console.error('Login Failed:', error));
Use code with caution.2. Using the Axios LibraryIf your frontend project uses axios, you don't need to worry about manual Base64 conversions. Axios features a built-in auth config option that handles the structural header injection automatically behind the scenes.javascriptimport axios from 'axios';

axios.get('http://localhost:8080/api/auth/login', {
    // Axios automatically computes the Base64 string and appends the proper header
    auth: {
        username: 'alice_smith',
        password: 'mypassword123'
    }
})
.then(response => {
    console.log('Login Success:', response.data);
})
.catch(error => {
    console.error('Login Failed:', error.response.status);
});
     */
}