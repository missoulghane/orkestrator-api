package com.m2it.orkestrator.infrastructure.adapter.in.web.mapper;

import com.m2it.orkestrator.application.dto.AuthenticationResult;
import com.m2it.orkestrator.application.dto.UserProfile;
import com.m2it.orkestrator.infrastructure.adapter.in.web.response.AuthenticationResponse;
import com.m2it.orkestrator.infrastructure.adapter.in.web.response.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationResponse toResponse(AuthenticationResult result) {
        return AuthenticationResponse.builder()
                .accessToken(result.getAccessToken())
                .refreshToken(result.getRefreshToken())
                .tokenType(result.getTokenType())
                .expiresIn(result.getExpiresIn())
                .build();
    }

    public UserProfileResponse toResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .email(profile.getEmail())
                .emailVerified(profile.isEmailVerified())
                .build();
    }

}
