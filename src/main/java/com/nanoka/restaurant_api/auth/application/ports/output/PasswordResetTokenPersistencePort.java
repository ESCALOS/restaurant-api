package com.nanoka.restaurant_api.auth.application.ports.output;

import com.nanoka.restaurant_api.auth.domain.model.PasswordResetToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenPersistencePort {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByUserId(Long userId);
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteExpiredTokens(LocalDateTime now);
}
