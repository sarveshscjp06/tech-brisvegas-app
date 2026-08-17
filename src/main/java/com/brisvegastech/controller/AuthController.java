package com.brisvegastech.controller;

import com.brisvegastech.dto.EnrollRequest;
import com.brisvegastech.service.EmailService;
import com.brisvegastech.service.UserService;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final UserService userService;

    @Autowired
    private EmailService emailService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollUser(@RequestBody EnrollRequest request) {
        try {
            String message = userService.enrollUser(request);

            if (message != null) {
                String body = "<p>Kindly click / tap on below link to verify your email address.</p>"
                        + "<a href=http://140.238.250.40:8888/api/auth/doemailverification?userName=" + request.getUsername() + "&email=" + request.getEmail() + "><b>" + request.getPassword() + "</b></a>";
                emailService.sendSimpleEmail(request.getEmail(), "BrivegasTech: email verification", body);
            }

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

    @RequestMapping(value = "/doemailverification", method = RequestMethod.GET)
    public String doVerifyEmail(@RequestParam("userName") String userName, @RequestParam("email") String email) {
        String verificationStatus = "e-mail verification failure. May be server is down. "
                .concat("Kindly contact to admin on email: sarvesh@brisvegastech.com");
        try {
            if (userService.updateUserForEmailVerification(userName, email)) {
                verificationStatus = "e-mail verification successful. Thank you!";
            }
        } catch (Exception ex) {
            Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificationStatus;
    }

    @RequestMapping(value = "/domobileverification", method = RequestMethod.GET)
    public String doVerifyMobile(@RequestParam("userName") String userName, @RequestParam("email") String email, @RequestParam("mobile") long mobile) {
        String verificationStatus = "mobile no verification failure. May be server is down. "
                .concat("Kindly contact to admin on mobile: +919312181442.");
        try {
            if (userService.updateUserForMobileVerification(userName, email, mobile)) {
                verificationStatus = "mobile verification successful. Thank you!";
            }
        } catch (Exception ex) {
            Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificationStatus;
    }
}