package com.m2it.orkestrator.domain.exception;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public static UserAlreadyExistsException byUsername(String username) {
        return new UserAlreadyExistsException("Un utilisateur avec le nom d'utilisateur '" + username + "' existe déjà");
    }

    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException("Un utilisateur avec l'email '" + email + "' existe déjà");
    }

}
