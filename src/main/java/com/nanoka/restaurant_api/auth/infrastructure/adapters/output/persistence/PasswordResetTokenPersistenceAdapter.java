package com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence;

import com.nanoka.restaurant_api.auth.application.ports.output.PasswordResetTokenPersistencePort;
import com.nanoka.restaurant_api.auth.domain.model.PasswordResetToken;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence.entity.PasswordResetTokenEntity;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence.mapper.PasswordResetTokenMapper;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.output.persistence.repository.PasswordResetTokenRepository;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PasswordResetTokenPersistenceAdapter implements PasswordResetTokenPersistencePort {

    private final PasswordResetTokenRepository repository;
    private final PasswordResetTokenMapper mapper;

    public PasswordResetTokenPersistenceAdapter(PasswordResetTokenRepository repository, PasswordResetTokenMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = mapper.toPasswordResetTokenEntity(token);
        return mapper.toPasswordResetToken(repository.save(entity));
    }

    @Override
    public Optional<PasswordResetToken> findByUserId(Long userId) {
        return repository.findByUserId(userId).map(mapper::toPasswordResetToken);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return repository.findByToken(token).map(mapper::toPasswordResetToken);
    }

    @Override
    public void deleteByToken(String token) {
        repository.findByToken(token).ifPresent(repository::delete);
    }

    @Override
    public void deleteExpiredTokens(LocalDateTime now) {
        repository.deleteByExpirationDateBefore(now);
    }
}
