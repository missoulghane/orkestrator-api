package com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository;

import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, UUID> {

    Optional<PermissionEntity> findByName(String name);

    Optional<PermissionEntity> findByResourceAndAction(String resource, String action);

    boolean existsByName(String name);

}
