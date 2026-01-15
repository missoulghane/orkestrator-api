package com.m2it.orkestrator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    private UUID id;
    private String name;
    private String description;
    private String resource;
    private String action;

    /**
     * Retourne le nom de l'autorité pour Spring Security.
     * Format: RESOURCE_ACTION (ex: USER_READ, USER_WRITE)
     */
    public String getAuthority() {
        return resource.toUpperCase() + "_" + action.toUpperCase();
    }

}
