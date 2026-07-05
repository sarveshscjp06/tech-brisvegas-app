package com.brisvegastech.registration.service;

import com.brisvegastech.registration.dto.RegistrationRequest;
import com.brisvegastech.registration.dto.RegistrationResponse;

import java.util.List;

public interface RegistrationService {

    RegistrationResponse register(RegistrationRequest request);

    RegistrationResponse getById(Long id);

    List<RegistrationResponse> getAll();

    RegistrationResponse update(Long id, RegistrationRequest request);

    void delete(Long id);
}
