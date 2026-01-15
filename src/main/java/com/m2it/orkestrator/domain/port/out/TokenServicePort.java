package com.m2it.orkestrator.domain.port.out;

import java.util.UUID;

public interface TokenServicePort {

    String generateAccessToken(UUID userId, String username);

    String generateRefreshToken();

    String generateVerificationToken();

    UUID extractUserId(String token);

    String extractUsername(String token);

    boolean validateAccessToken(String token);

}
