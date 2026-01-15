package com.m2it.orkestrator.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException byId(UUID id) {
        return new UserNotFoundException("Utilisateur non trouvé avec l'id: " + id);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email);
    }

    public static UserNotFoundException byUsernameOrEmail(String usernameOrEmail) {
        return new UserNotFoundException("Utilisateur non trouvé: " + usernameOrEmail);
    }

}
