package com.nanoka.restaurant_api.auth.application.ports.input;

import com.nanoka.restaurant_api.auth.domain.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenServicePort {
    PasswordResetToken createToken(Long userId);
    Optional<PasswordResetToken> validateToken(String token);
    void deleteToken(String token);
    void resetPassword(String token, String newPassword);
}
