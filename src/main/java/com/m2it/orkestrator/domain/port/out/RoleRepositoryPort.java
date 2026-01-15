package com.m2it.orkestrator.domain.port.out;

import com.m2it.orkestrator.domain.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepositoryPort {

    Role save(Role role);

    Optional<Role> findById(UUID id);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    boolean existsByName(String name);

    void deleteById(UUID id);

}
