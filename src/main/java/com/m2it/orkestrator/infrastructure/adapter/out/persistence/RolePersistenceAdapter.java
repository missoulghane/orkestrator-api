package com.m2it.orkestrator.infrastructure.adapter.out.persistence;

import com.m2it.orkestrator.domain.model.Role;
import com.m2it.orkestrator.domain.port.out.RoleRepositoryPort;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.mapper.RolePersistenceMapper;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RoleRepositoryPort {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePersistenceMapper roleMapper;

    @Override
    public Role save(Role role) {
        RoleEntity entity = roleMapper.toEntity(role);
        RoleEntity savedEntity = roleJpaRepository.save(entity);
        return roleMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id)
                .map(roleMapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name)
                .map(roleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(roleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(UUID id) {
        roleJpaRepository.deleteById(id);
    }

}
