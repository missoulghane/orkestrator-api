package com.m2it.orkestrator.infrastructure.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Profil de l'utilisateur connecté")
public class UserProfileResponse {

    @Schema(description = "Identifiant unique de l'utilisateur", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nom d'utilisateur", example = "johndoe")
    private String username;

    @Schema(description = "Prénom de l'utilisateur", example = "John")
    private String firstName;

    @Schema(description = "Nom de l'utilisateur", example = "Doe")
    private String lastName;

    @Schema(description = "Adresse email", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Indique si l'email a été vérifié", example = "true")
    private boolean emailVerified;

}
