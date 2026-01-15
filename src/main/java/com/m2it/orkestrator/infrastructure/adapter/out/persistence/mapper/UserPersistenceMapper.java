package com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper;

import com.m2it.orkestrator.domain.model.Role;
import com.m2it.orkestrator.domain.model.User;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPersistenceMapper {

    private final RolePersistenceMapper roleMapper;

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Role> roles = entity.getRoles().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toSet());

        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .emailVerified(entity.isEmailVerified())
                .enabled(entity.isEnabled())
                .accountLocked(entity.isAccountLocked())
                .roles(roles)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }

        Set<RoleEntity> roles = domain.getRoles().stream()
                .map(roleMapper::toEntity)
                .collect(Collectors.toSet());

        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .emailVerified(domain.isEmailVerified())
                .enabled(domain.isEnabled())
                .accountLocked(domain.isAccountLocked())
                .roles(roles)
                .build();
    }

}
