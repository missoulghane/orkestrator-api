package com.m2it.orkestrator.application.service;

import com.m2it.orkestrator.application.command.ForgotPasswordCommand;
import com.m2it.orkestrator.application.command.LoginCommand;
import com.m2it.orkestrator.application.command.LogoutCommand;
import com.m2it.orkestrator.application.command.RefreshTokenCommand;
import com.m2it.orkestrator.application.command.RegisterUserCommand;
import com.m2it.orkestrator.application.command.ResetPasswordCommand;
import com.m2it.orkestrator.application.command.VerifyEmailCommand;
import com.m2it.orkestrator.application.dto.AuthenticationResult;
import com.m2it.orkestrator.application.dto.UserProfile;
import com.m2it.orkestrator.domain.exception.InvalidCredentialsException;
import com.m2it.orkestrator.domain.exception.InvalidTokenException;
import com.m2it.orkestrator.domain.exception.UserAlreadyExistsException;
import com.m2it.orkestrator.domain.exception.UserNotFoundException;
import com.m2it.orkestrator.domain.model.RefreshToken;
import com.m2it.orkestrator.domain.model.Role;
import com.m2it.orkestrator.domain.model.User;
import com.m2it.orkestrator.domain.model.VerificationToken;
import com.m2it.orkestrator.domain.port.in.AuthenticationUseCase;
import com.m2it.orkestrator.domain.port.out.EmailServicePort;
import com.m2it.orkestrator.domain.port.out.RefreshTokenRepositoryPort;
import com.m2it.orkestrator.domain.port.out.RoleRepositoryPort;
import com.m2it.orkestrator.domain.port.out.TokenServicePort;
import com.m2it.orkestrator.domain.port.out.UserRepositoryPort;
import com.m2it.orkestrator.domain.port.out.VerificationTokenRepositoryPort;
import com.m2it.orkestrator.infrastructure.adapter.out.token.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final VerificationTokenRepositoryPort verificationTokenRepository;
    private final TokenServicePort tokenService;
    private final EmailServicePort emailService;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_USER_ROLE = "USER";

    @Value("${jwt.refresh-token-expiration-days:7}")
    private int refreshTokenExpirationDays;

    @Value("${jwt.verification-token-expiration-hours:24}")
    private int verificationTokenExpirationHours;

    @Override
    @Transactional
    public AuthenticationResult register(RegisterUserCommand command) {
        log.info("Registering new user with username: {}", command.getUsername());

        if (userRepository.existsByUsername(command.getUsername())) {
            throw UserAlreadyExistsException.byUsername(command.getUsername());
        }

        if (userRepository.existsByEmail(command.getEmail())) {
            throw UserAlreadyExistsException.byEmail(command.getEmail());
        }

        Role defaultRole = roleRepository.findByName(DEFAULT_USER_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default role '" + DEFAULT_USER_ROLE + "' not found"));

        User user = User.builder()
                .username(command.getUsername())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .emailVerified(false)
                .enabled(true)
                .accountLocked(false)
                .build();
        user.addRole(defaultRole);

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());

        String verificationToken = tokenService.generateVerificationToken();
        VerificationToken token = VerificationToken.builder()
                .token(verificationToken)
                .userId(savedUser.getId())
                .type(VerificationToken.TokenType.EMAIL_VERIFICATION)
                .expiresAt(LocalDateTime.now().plusHours(verificationTokenExpirationHours))
                .used(false)
                .build();
        verificationTokenRepository.save(token);

        emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);

        return generateAuthenticationResult(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResult login(LoginCommand command) {
        log.info("User login attempt: {}", command.getUsernameOrEmail());

        User user = userRepository.findByUsernameOrEmail(command.getUsernameOrEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("Ce compte est désactivé");
        }

        if (user.isAccountLocked()) {
            throw new InvalidCredentialsException("Ce compte est verrouillé");
        }

        log.info("User logged in successfully: {}", user.getUsername());
        return generateAuthenticationResult(user);
    }

    @Override
    @Transactional
    public AuthenticationResult refreshToken(RefreshTokenCommand command) {
        log.info("Refreshing token");

        RefreshToken storedToken = refreshTokenRepository.findByToken(command.getRefreshToken())
                .orElseThrow(InvalidTokenException::invalid);

        if (!storedToken.isValid()) {
            if (storedToken.isExpired()) {
                throw InvalidTokenException.expired();
            }
            throw InvalidTokenException.invalid();
        }

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> UserNotFoundException.byId(storedToken.getUserId()));

        refreshTokenRepository.revokeAllByUserId(user.getId());

        log.info("Token refreshed for user: {}", user.getUsername());
        return generateAuthenticationResult(user);
    }

    @Override
    @Transactional
    public void verifyEmail(VerifyEmailCommand command) {
        log.info("Verifying email with token");

        VerificationToken token = verificationTokenRepository.findByToken(command.getToken())
                .orElseThrow(InvalidTokenException::invalid);

        if (!token.isValid()) {
            if (token.isExpired()) {
                throw InvalidTokenException.expired();
            }
            if (token.isUsed()) {
                throw InvalidTokenException.alreadyUsed();
            }
            throw InvalidTokenException.invalid();
        }

        if (token.getType() != VerificationToken.TokenType.EMAIL_VERIFICATION) {
            throw InvalidTokenException.invalid();
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> UserNotFoundException.byId(token.getUserId()));

        user.setEmailVerified(true);
        userRepository.save(user);

        token.setUsed(true);
        verificationTokenRepository.save(token);

        log.info("Email verified successfully for user: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordCommand command) {
        log.info("Password reset requested for email: {}", command.getEmail());

        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> UserNotFoundException.byEmail(command.getEmail()));

        String resetToken = tokenService.generateVerificationToken();
        VerificationToken token = VerificationToken.builder()
                .token(resetToken)
                .userId(user.getId())
                .type(VerificationToken.TokenType.PASSWORD_RESET)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();
        verificationTokenRepository.save(token);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        log.info("Password reset email sent to: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordCommand command) {
        log.info("Resetting password with token");

        VerificationToken token = verificationTokenRepository.findByToken(command.getToken())
                .orElseThrow(InvalidTokenException::invalid);

        if (!token.isValid()) {
            if (token.isExpired()) {
                throw InvalidTokenException.expired();
            }
            if (token.isUsed()) {
                throw InvalidTokenException.alreadyUsed();
            }
            throw InvalidTokenException.invalid();
        }

        if (token.getType() != VerificationToken.TokenType.PASSWORD_RESET) {
            throw InvalidTokenException.invalid();
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> UserNotFoundException.byId(token.getUserId()));

        user.setPassword(passwordEncoder.encode(command.getNewPassword()));
        userRepository.save(user);

        token.setUsed(true);
        verificationTokenRepository.save(token);

        refreshTokenRepository.revokeAllByUserId(user.getId());

        log.info("Password reset successfully for user: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void logout(LogoutCommand command) {
        log.info("Logout requested");

        RefreshToken storedToken = refreshTokenRepository.findByToken(command.getRefreshToken())
                .orElseThrow(InvalidTokenException::invalid);

        refreshTokenRepository.revokeAllByUserId(storedToken.getUserId());

        log.info("User logged out successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfile getCurrentUser(UUID userId) {
        log.info("Getting current user profile for id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));

        return UserProfile.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .build();
    }

    private AuthenticationResult generateAuthenticationResult(User user) {
        String accessToken = tokenService.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = tokenService.generateRefreshToken();

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusDays(refreshTokenExpirationDays))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        long expiresIn = 3600L;
        if (tokenService instanceof JwtTokenService jwtService) {
            expiresIn = jwtService.getAccessTokenExpirationSeconds();
        }

        return AuthenticationResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .build();
    }

}
