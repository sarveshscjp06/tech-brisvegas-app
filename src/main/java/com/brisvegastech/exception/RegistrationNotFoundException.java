package com.brisvegastech.exception;

public class RegistrationNotFoundException extends RuntimeException {
    public RegistrationNotFoundException(Long id) {
        super("Registration not found with id: " + id);
    }
}
