package com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper;

import com.m2it.orkestrator.domain.model.VerificationToken;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.VerificationTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenPersistenceMapper {

    public VerificationToken toDomain(VerificationTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        return VerificationToken.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .userId(entity.getUser().getId())
                .type(mapTypeToDomain(entity.getType()))
                .expiresAt(entity.getExpiresAt())
                .used(entity.isUsed())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public VerificationTokenEntity toEntity(VerificationToken domain, UserEntity user) {
        if (domain == null) {
            return null;
        }

        return VerificationTokenEntity.builder()
                .id(domain.getId())
                .token(domain.getToken())
                .user(user)
                .type(mapTypeToEntity(domain.getType()))
                .expiresAt(domain.getExpiresAt())
                .used(domain.isUsed())
                .build();
    }

    private VerificationToken.TokenType mapTypeToDomain(VerificationTokenEntity.TokenType entityType) {
        return switch (entityType) {
            case EMAIL_VERIFICATION -> VerificationToken.TokenType.EMAIL_VERIFICATION;
            case PASSWORD_RESET -> VerificationToken.TokenType.PASSWORD_RESET;
        };
    }

    private VerificationTokenEntity.TokenType mapTypeToEntity(VerificationToken.TokenType domainType) {
        return switch (domainType) {
            case EMAIL_VERIFICATION -> VerificationTokenEntity.TokenType.EMAIL_VERIFICATION;
            case PASSWORD_RESET -> VerificationTokenEntity.TokenType.PASSWORD_RESET;
        };
    }

}
