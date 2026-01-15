package com.m2it.orkestrator.domain.port.out;

import com.m2it.orkestrator.domain.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepositoryPort {

    VerificationToken save(VerificationToken token);

    Optional<VerificationToken> findByToken(String token);

    void deleteExpiredTokens();

}
