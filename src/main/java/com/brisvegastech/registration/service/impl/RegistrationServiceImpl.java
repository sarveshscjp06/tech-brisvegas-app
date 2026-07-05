package com.brisvegastech.registration.service.impl;

import com.brisvegastech.registration.dto.RegistrationRequest;
import com.brisvegastech.registration.dto.RegistrationResponse;
import com.brisvegastech.registration.entity.RegistrationEntity;
import com.brisvegastech.registration.exception.EmailAlreadyExistsException;
import com.brisvegastech.registration.exception.RegistrationNotFoundException;
import com.brisvegastech.registration.repository.RegistrationRepository;
import com.brisvegastech.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;

    // Lightweight hashing without pulling in full Spring Security starter.
    // Swap for your organisation's approved password hashing utility if different.
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (registrationRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        RegistrationEntity entity = RegistrationEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(RegistrationEntity.RegistrationStatus.PENDING_VERIFICATION)
                .build();

        RegistrationEntity saved = registrationRepository.save(entity);
        log.info("Registration successful, id={}", saved.getId());
        return RegistrationResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RegistrationResponse getById(Long id) {
        RegistrationEntity entity = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));
        return RegistrationResponse.fromEntity(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getAll() {
        return registrationRepository.findAll().stream()
                .map(RegistrationResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public RegistrationResponse update(Long id, RegistrationRequest request) {
        RegistrationEntity entity = registrationRepository.findById(id)
                .orElseThrow(() -> new RegistrationNotFoundException(id));

        if (!entity.getEmail().equalsIgnoreCase(request.getEmail())
                && registrationRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setPhoneNumber(request.getPhoneNumber());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            entity.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        RegistrationEntity updated = registrationRepository.save(entity);
        return RegistrationResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!registrationRepository.existsById(id)) {
            throw new RegistrationNotFoundException(id);
        }
        registrationRepository.deleteById(id);
        log.info("Deleted registration id={}", id);
    }
}
