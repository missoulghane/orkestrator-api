package com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper;

import com.m2it.orkestrator.domain.model.Permission;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class PermissionPersistenceMapper {

    public Permission toDomain(PermissionEntity entity) {
        if (entity == null) {
            return null;
        }

        return Permission.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .resource(entity.getResource())
                .action(entity.getAction())
                .build();
    }

    public PermissionEntity toEntity(Permission domain) {
        if (domain == null) {
            return null;
        }

        return PermissionEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .resource(domain.getResource())
                .action(domain.getAction())
                .build();
    }

}
