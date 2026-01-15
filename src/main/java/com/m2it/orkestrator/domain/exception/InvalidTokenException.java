package com.m2it.orkestrator.domain.exception;

public class InvalidTokenException extends DomainException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public static InvalidTokenException expired() {
        return new InvalidTokenException("Le token a expiré");
    }

    public static InvalidTokenException invalid() {
        return new InvalidTokenException("Token invalide");
    }

    public static InvalidTokenException alreadyUsed() {
        return new InvalidTokenException("Ce token a déjà été utilisé");
    }

}
