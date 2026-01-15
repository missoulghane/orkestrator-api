package com.m2it.orkestrator.domain.exception;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Identifiants invalides");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
