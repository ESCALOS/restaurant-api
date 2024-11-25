package com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByToken(String token);
    Optional<PasswordResetTokenEntity> findByUserId(Long userId);
    void deleteByExpirationDateBefore(LocalDateTime now);
}
