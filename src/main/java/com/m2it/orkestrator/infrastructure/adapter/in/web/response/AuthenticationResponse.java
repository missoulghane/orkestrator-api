package com.m2it.orkestrator.infrastructure.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Réponse d'authentification contenant les tokens")
public class AuthenticationResponse {

    @Schema(description = "Token d'accès JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Token de rafraîchissement", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
    private String refreshToken;

    @Schema(description = "Type de token", example = "Bearer")
    private String tokenType;

    @Schema(description = "Durée de validité du token d'accès en secondes", example = "3600")
    private Long expiresIn;

}
