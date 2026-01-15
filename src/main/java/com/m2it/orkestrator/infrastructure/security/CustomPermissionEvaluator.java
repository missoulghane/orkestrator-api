package com.m2it.orkestrator.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Évaluateur de permissions personnalisé pour Spring Security.
 * Permet d'utiliser @PreAuthorize("hasPermission(#target, 'READ')") dans les contrôleurs.
 */
@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    /**
     * Évalue si l'utilisateur a la permission spécifiée sur la cible.
     *
     * @param authentication L'authentification de l'utilisateur
     * @param targetDomainObject L'objet cible (ex: "USER", "ORDER")
     * @param permission La permission requise (ex: "READ", "WRITE", "DELETE")
     * @return true si l'utilisateur a la permission
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || permission == null) {
            return false;
        }

        String targetType = targetDomainObject.toString().toUpperCase();
        String permissionName = permission.toString().toUpperCase();
        String requiredAuthority = targetType + "_" + permissionName;

        boolean hasPermission = hasAuthority(authentication, requiredAuthority);

        log.debug("Permission check: user={}, target={}, permission={}, required={}, result={}",
                authentication.getName(), targetType, permissionName, requiredAuthority, hasPermission);

        return hasPermission;
    }

    /**
     * Évalue si l'utilisateur a la permission spécifiée sur un objet identifié par son ID.
     *
     * @param authentication L'authentification de l'utilisateur
     * @param targetId L'identifiant de l'objet cible
     * @param targetType Le type de l'objet cible
     * @param permission La permission requise
     * @return true si l'utilisateur a la permission
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                 String targetType, Object permission) {
        if (authentication == null || targetType == null || permission == null) {
            return false;
        }

        String permissionName = permission.toString().toUpperCase();
        String requiredAuthority = targetType.toUpperCase() + "_" + permissionName;

        boolean hasPermission = hasAuthority(authentication, requiredAuthority);

        log.debug("Permission check by ID: user={}, targetId={}, targetType={}, permission={}, result={}",
                authentication.getName(), targetId, targetType, permissionName, hasPermission);

        return hasPermission;
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals(authority));
    }

}
