package com.example.registration.service;

import com.example.registration.dto.RegistrationRequest;
import com.example.registration.dto.RegistrationResponse;

import java.util.List;

public interface RegistrationService {

    RegistrationResponse register(RegistrationRequest request);

    RegistrationResponse getById(Long id);

    List<RegistrationResponse> getAll();

    RegistrationResponse update(Long id, RegistrationRequest request);

    void delete(Long id);
}
