package com.m2it.orkestrator.infrastructure.config;

import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.PermissionEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.RoleEntity;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.PermissionJpaRepository;
import com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Initialise les données par défaut au démarrage de l'application.
 * Crée les rôles et permissions de base.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleJpaRepository roleRepository;
    private final PermissionJpaRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing default roles and permissions...");

        // Create permissions
        PermissionEntity userRead = createPermissionIfNotExists("USER_READ", "Lecture des utilisateurs", "USER", "READ");
        PermissionEntity userWrite = createPermissionIfNotExists("USER_WRITE", "Modification des utilisateurs", "USER", "WRITE");
        PermissionEntity userDelete = createPermissionIfNotExists("USER_DELETE", "Suppression des utilisateurs", "USER", "DELETE");

        PermissionEntity roleRead = createPermissionIfNotExists("ROLE_READ", "Lecture des rôles", "ROLE", "READ");
        PermissionEntity roleWrite = createPermissionIfNotExists("ROLE_WRITE", "Modification des rôles", "ROLE", "WRITE");
        PermissionEntity roleDelete = createPermissionIfNotExists("ROLE_DELETE", "Suppression des rôles", "ROLE", "DELETE");

        PermissionEntity settingsRead = createPermissionIfNotExists("SETTINGS_READ", "Lecture des paramètres", "SETTINGS", "READ");
        PermissionEntity settingsWrite = createPermissionIfNotExists("SETTINGS_WRITE", "Modification des paramètres", "SETTINGS", "WRITE");

        // Create roles with permissions
        Set<PermissionEntity> adminPermissions = new HashSet<>();
        adminPermissions.add(userRead);
        adminPermissions.add(userWrite);
        adminPermissions.add(userDelete);
        adminPermissions.add(roleRead);
        adminPermissions.add(roleWrite);
        adminPermissions.add(roleDelete);
        adminPermissions.add(settingsRead);
        adminPermissions.add(settingsWrite);
        createRoleIfNotExists("ADMIN", "Administrateur avec tous les droits", adminPermissions);

        Set<PermissionEntity> moderatorPermissions = new HashSet<>();
        moderatorPermissions.add(userRead);
        moderatorPermissions.add(userWrite);
        moderatorPermissions.add(settingsRead);
        createRoleIfNotExists("MODERATOR", "Modérateur avec droits de gestion des utilisateurs", moderatorPermissions);

        Set<PermissionEntity> userPermissions = new HashSet<>();
        userPermissions.add(userRead);
        createRoleIfNotExists("USER", "Utilisateur standard", userPermissions);

        log.info("Default roles and permissions initialized successfully");
    }

    private PermissionEntity createPermissionIfNotExists(String name, String description, String resource, String action) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    PermissionEntity permission = PermissionEntity.builder()
                            .name(name)
                            .description(description)
                            .resource(resource)
                            .action(action)
                            .build();
                    log.info("Creating permission: {}", name);
                    return permissionRepository.save(permission);
                });
    }

    private void createRoleIfNotExists(String name, String description, Set<PermissionEntity> permissions) {
        if (!roleRepository.existsByName(name)) {
            RoleEntity role = RoleEntity.builder()
                    .name(name)
                    .description(description)
                    .permissions(permissions)
                    .build();
            roleRepository.save(role);
            log.info("Created role: {} with {} permissions", name, permissions.size());
        }
    }

}
