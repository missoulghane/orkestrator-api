package com.m2it.orkestrator.infrastructure.adapter.out.persistence;

import com.m2it.orkestrator.domain.model.VerificationToken;
import com.m2it.orkestrator.domain.port.out.VerificationTokenRepositoryPort;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.VerificationTokenEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper.VerificationTokenPersistenceMapper;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.VerificationTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerificationTokenPersistenceAdapter implements VerificationTokenRepositoryPort {

    private final VerificationTokenJpaRepository verificationTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final VerificationTokenPersistenceMapper verificationTokenMapper;

    @Override
    public VerificationToken save(VerificationToken token) {
        UserEntity user = userJpaRepository.getReferenceById(token.getUserId());
        VerificationTokenEntity entity = verificationTokenMapper.toEntity(token, user);
        VerificationTokenEntity savedEntity = verificationTokenJpaRepository.save(entity);
        return verificationTokenMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenJpaRepository.findByToken(token)
                .map(verificationTokenMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        verificationTokenJpaRepository.deleteExpiredTokens(LocalDateTime.now());
    }

}
