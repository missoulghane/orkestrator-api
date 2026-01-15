package com.m2it.orkestrator.domain.port.out;

import com.m2it.orkestrator.domain.model.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepositoryPort {

    Permission save(Permission permission);

    Optional<Permission> findById(UUID id);

    Optional<Permission> findByName(String name);

    Optional<Permission> findByResourceAndAction(String resource, String action);

    List<Permission> findAll();

    boolean existsByName(String name);

    void deleteById(UUID id);

}
