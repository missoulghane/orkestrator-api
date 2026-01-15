package com.m2it.orkestrator.domain.port.in;

import com.m2it.orkestrator.application.command.ForgotPasswordCommand;
import com.m2it.orkestrator.application.command.LoginCommand;
import com.m2it.orkestrator.application.command.LogoutCommand;
import com.m2it.orkestrator.application.command.RefreshTokenCommand;
import com.m2it.orkestrator.application.command.RegisterUserCommand;
import com.m2it.orkestrator.application.command.ResetPasswordCommand;
import com.m2it.orkestrator.application.command.VerifyEmailCommand;
import com.m2it.orkestrator.application.dto.AuthenticationResult;
import com.m2it.orkestrator.application.dto.UserProfile;

import java.util.UUID;

public interface AuthenticationUseCase {

    AuthenticationResult register(RegisterUserCommand command);

    AuthenticationResult login(LoginCommand command);

    AuthenticationResult refreshToken(RefreshTokenCommand command);

    void verifyEmail(VerifyEmailCommand command);

    void forgotPassword(ForgotPasswordCommand command);

    void resetPassword(ResetPasswordCommand command);

    void logout(LogoutCommand command);

    UserProfile getCurrentUser(UUID userId);

}
