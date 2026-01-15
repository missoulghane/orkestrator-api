package com.m2it.orkestrator.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResult {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;

}
