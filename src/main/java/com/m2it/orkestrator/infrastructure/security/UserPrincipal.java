package com.m2it.orkestrator.infrastructure.security;

import com.m2it.orkestrator.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final UUID userId;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean accountLocked;
    private final boolean emailVerified;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return UserPrincipal.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .accountLocked(user.isAccountLocked())
                .emailVerified(user.isEmailVerified())
                .authorities(authorities)
                .build();
    }

    public static UserPrincipal create(UUID userId, String username, String password,
                                        boolean enabled, Set<String> authorityStrings) {
        Set<GrantedAuthority> authorities = authorityStrings.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return UserPrincipal.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .enabled(enabled)
                .accountLocked(false)
                .emailVerified(true)
                .authorities(authorities)
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean hasRole(String role) {
        String roleAuthority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(roleAuthority));
    }

    public boolean hasPermission(String permission) {
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }

}
