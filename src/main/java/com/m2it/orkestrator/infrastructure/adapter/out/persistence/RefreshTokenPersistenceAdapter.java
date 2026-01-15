package com.m2it.orkestrator.infrastructure.adapter.out.persistence;

import com.m2it.orkestrator.domain.model.RefreshToken;
import com.m2it.orkestrator.domain.port.out.RefreshTokenRepositoryPort;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.RefreshTokenEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper.RefreshTokenPersistenceMapper;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.RefreshTokenJpaRepository;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenPersistenceMapper refreshTokenMapper;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        UserEntity user = userJpaRepository.getReferenceById(refreshToken.getUserId());
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken, user);
        RefreshTokenEntity savedEntity = refreshTokenJpaRepository.save(entity);
        return refreshTokenMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token)
                .map(refreshTokenMapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(UUID userId) {
        refreshTokenJpaRepository.revokeAllByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenJpaRepository.deleteExpiredTokens(LocalDateTime.now());
    }

}
