package com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper;

import com.m2it.orkestrator.domain.model.RefreshToken;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.RefreshTokenEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenPersistenceMapper {

    public RefreshToken toDomain(RefreshTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        return RefreshToken.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .userId(entity.getUser().getId())
                .expiresAt(entity.getExpiresAt())
                .revoked(entity.isRevoked())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public RefreshTokenEntity toEntity(RefreshToken domain, UserEntity user) {
        if (domain == null) {
            return null;
        }

        return RefreshTokenEntity.builder()
                .id(domain.getId())
                .token(domain.getToken())
                .user(user)
                .expiresAt(domain.getExpiresAt())
                .revoked(domain.isRevoked())
                .build();
    }

}
