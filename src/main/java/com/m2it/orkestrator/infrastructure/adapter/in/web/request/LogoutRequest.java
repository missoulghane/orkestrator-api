package com.m2it.orkestrator.infrastructure.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Requête de déconnexion")
public class LogoutRequest {

    @NotBlank(message = "Le refresh token est obligatoire")
    @Schema(description = "Refresh token à révoquer")
    private String refreshToken;

}
