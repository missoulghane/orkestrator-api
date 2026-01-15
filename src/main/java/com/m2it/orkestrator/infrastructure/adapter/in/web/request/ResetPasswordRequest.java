package com.m2it.orkestrator.infrastructure.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Requête de réinitialisation de mot de passe")
public class ResetPasswordRequest {

    @NotBlank(message = "Le token est obligatoire")
    @Schema(description = "Token de réinitialisation reçu par email")
    private String token;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 8, max = 100, message = "Le mot de passe doit contenir entre 8 et 100 caractères")
    @Schema(description = "Nouveau mot de passe (min 8 caractères)", example = "NewSecureP@ss123")
    private String newPassword;

}
