package com.example.registration.controller;

import com.example.registration.dto.RegistrationRequest;
import com.example.registration.dto.RegistrationResponse;
import com.example.registration.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RegistrationResponse>> getAll() {
        return ResponseEntity.ok(registrationService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistrationResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        registrationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
