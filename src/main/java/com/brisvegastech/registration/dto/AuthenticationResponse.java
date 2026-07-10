package com.brisvegastech.registration.dto;

public record AuthenticationResponse(
    String token,
    String tokenType,
    long expiresInMillis
) {}