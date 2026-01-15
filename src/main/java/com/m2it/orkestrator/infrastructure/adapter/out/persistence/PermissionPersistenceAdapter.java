package com.m2it.orkestrator.infrastructure.adapter.out.persistence;

import com.m2it.orkestrator.domain.model.Permission;
import com.m2it.orkestrator.domain.port.out.PermissionRepositoryPort;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper.PermissionPersistenceMapper;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionPersistenceAdapter implements PermissionRepositoryPort {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionPersistenceMapper permissionMapper;

    @Override
    public Permission save(Permission permission) {
        PermissionEntity entity = permissionMapper.toEntity(permission);
        PermissionEntity savedEntity = permissionJpaRepository.save(entity);
        return permissionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Permission> findById(UUID id) {
        return permissionJpaRepository.findById(id)
                .map(permissionMapper::toDomain);
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionJpaRepository.findByName(name)
                .map(permissionMapper::toDomain);
    }

    @Override
    public Optional<Permission> findByResourceAndAction(String resource, String action) {
        return permissionJpaRepository.findByResourceAndAction(resource, action)
                .map(permissionMapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionJpaRepository.findAll().stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(UUID id) {
        permissionJpaRepository.deleteById(id);
    }

}
