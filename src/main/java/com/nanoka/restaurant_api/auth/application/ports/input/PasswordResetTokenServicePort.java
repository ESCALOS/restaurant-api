package com.nanoka.restaurant_api.auth.application.ports.input;

import com.nanoka.restaurant_api.auth.domain.model.PasswordResetToken;
import jakarta.mail.MessagingException;

import java.util.Optional;

public interface PasswordResetTokenServicePort {
    void createToken(String email) throws MessagingException;
    Optional<PasswordResetToken> validateToken(String token);
    void deleteToken(String token);
    void resetPassword(String token, String newPassword);
}
