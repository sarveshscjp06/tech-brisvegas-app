package com.example.registration.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("A registration with email '" + email + "' already exists");
    }
}
