package com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper;

import com.m2it.orkestrator.domain.model.Permission;
import com.m2it.orkestrator.domain.model.Role;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RolePersistenceMapper {

    private final PermissionPersistenceMapper permissionMapper;

    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Permission> permissions = entity.getPermissions().stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toSet());

        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .permissions(permissions)
                .build();
    }

    public RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }

        Set<PermissionEntity> permissions = domain.getPermissions().stream()
                .map(permissionMapper::toEntity)
                .collect(Collectors.toSet());

        return RoleEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .permissions(permissions)
                .build();
    }

}
