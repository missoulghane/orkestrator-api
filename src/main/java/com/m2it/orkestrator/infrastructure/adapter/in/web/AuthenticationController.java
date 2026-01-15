package com.m2it.orkestrator.infrastructure.adapter.in.web;

import com.m2it.orkestrator.application.command.ForgotPasswordCommand;
import com.m2it.orkestrator.application.command.LoginCommand;
import com.m2it.orkestrator.application.command.LogoutCommand;
import com.m2it.orkestrator.application.command.RefreshTokenCommand;
import com.m2it.orkestrator.application.command.RegisterUserCommand;
import com.m2it.orkestrator.application.command.ResetPasswordCommand;
import com.m2it.orkestrator.application.command.VerifyEmailCommand;
import com.m2it.orkestrator.application.dto.AuthenticationResult;
import com.m2it.orkestrator.application.dto.UserProfile;
import com.m2it.orkestrator.domain.port.in.AuthenticationUseCase;
import com.m2it.orkestrator.infrastructure.adapter.in.web.mapper.AuthenticationMapper;
import com.m2it.orkestrator.infrastructure.adapter.in.web.request.ForgotPasswordRequest;
import com.m2it.orkestrator.infrastructure.adapter.in.web.request.LoginRequest;
import com.m2it.orkestrator.infrastructure.adapter.in.web.request.LogoutRequest;
import com.m2it.orkestrator.infrastructure.adapter.in.web.request.RefreshTokenRequest;
import com.m2it.orkestrator.infrastructure.adapter.in.web.request.RegisterRequest;
import com.m2it.orkestrator.infrastructure.adapter.in.web.request.ResetPasswordRequest;
import com.m2it.orkestrator.infrastructure.adapter.in.web.response.AuthenticationResponse;
import com.m2it.orkestrator.infrastructure.adapter.in.web.response.UserProfileResponse;
import com.m2it.orkestrator.infrastructure.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints pour l'authentification et la gestion des sessions")
public class AuthenticationController {

    private final AuthenticationUseCase authenticationUseCase;
    private final AuthenticationMapper authenticationMapper;

    @PostMapping("/register")
    @Operation(
            summary = "Inscription d'un nouvel utilisateur",
            description = "Crée un nouveau compte utilisateur et envoie un email de vérification. Retourne un access token et un refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
            @ApiResponse(responseCode = "409", description = "Username ou email déjà existant", content = @Content)
    })
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserCommand command = RegisterUserCommand.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        AuthenticationResult result = authenticationUseCase.register(command);
        AuthenticationResponse response = authenticationMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Connexion",
            description = "Authentifie un utilisateur avec son username/email et mot de passe. Retourne un access token et un refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides", content = @Content)
    })
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = LoginCommand.builder()
                .usernameOrEmail(request.getUsernameOrEmail())
                .password(request.getPassword())
                .build();

        AuthenticationResult result = authenticationUseCase.login(command);
        AuthenticationResponse response = authenticationMapper.toResponse(result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Rafraîchir le token d'accès",
            description = "Utilise un refresh token valide pour obtenir un nouveau access token et un nouveau refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token rafraîchi avec succès",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token invalide ou expiré", content = @Content)
    })
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenCommand command = RefreshTokenCommand.builder()
                .refreshToken(request.getRefreshToken())
                .build();

        AuthenticationResult result = authenticationUseCase.refreshToken(command);
        AuthenticationResponse response = authenticationMapper.toResponse(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    @Operation(
            summary = "Vérifier l'adresse email",
            description = "Valide l'adresse email d'un utilisateur à partir du token envoyé par email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email vérifié avec succès", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token de vérification invalide ou expiré", content = @Content)
    })
    public ResponseEntity<Void> verifyEmail(
            @Parameter(description = "Token de vérification envoyé par email", required = true)
            @RequestParam String token) {
        VerifyEmailCommand command = VerifyEmailCommand.builder()
                .token(token)
                .build();

        authenticationUseCase.verifyEmail(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Demande de réinitialisation de mot de passe",
            description = "Envoie un email avec un lien pour réinitialiser le mot de passe"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email de réinitialisation envoyé avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content)
    })
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ForgotPasswordCommand command = ForgotPasswordCommand.builder()
                .email(request.getEmail())
                .build();

        authenticationUseCase.forgotPassword(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Réinitialiser le mot de passe",
            description = "Réinitialise le mot de passe avec le token reçu par email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mot de passe réinitialisé avec succès", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token de réinitialisation invalide ou expiré", content = @Content)
    })
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ResetPasswordCommand command = ResetPasswordCommand.builder()
                .token(request.getToken())
                .newPassword(request.getNewPassword())
                .build();

        authenticationUseCase.resetPassword(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Déconnexion",
            description = "Révoque le refresh token de l'utilisateur, l'empêchant de générer de nouveaux access tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Déconnexion réussie", content = @Content),
            @ApiResponse(responseCode = "401", description = "Refresh token invalide", content = @Content)
    })
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        LogoutCommand command = LogoutCommand.builder()
                .refreshToken(request.getRefreshToken())
                .build();

        authenticationUseCase.logout(command);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(
            summary = "Récupérer le profil utilisateur",
            description = "Retourne les détails (id, nom, prénom, email, username) de l'utilisateur actuellement connecté.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        UserProfile profile = authenticationUseCase.getCurrentUser(principal.getUserId());
        UserProfileResponse response = authenticationMapper.toResponse(profile);

        return ResponseEntity.ok(response);
    }

}
