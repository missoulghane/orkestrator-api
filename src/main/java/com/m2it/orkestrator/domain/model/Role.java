package com.m2it.orkestrator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private UUID id;
    private String name;
    private String description;

    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    /**
     * Retourne le nom du rôle pour Spring Security.
     * Format: ROLE_NAME (ex: ROLE_ADMIN, ROLE_USER)
     */
    public String getAuthority() {
        return "ROLE_" + name.toUpperCase();
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

}
