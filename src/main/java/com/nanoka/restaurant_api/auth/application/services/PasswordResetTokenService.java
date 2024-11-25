package com.nanoka.restaurant_api.auth.application.services;

import com.nanoka.restaurant_api.auth.application.ports.input.PasswordResetTokenServicePort;
import com.nanoka.restaurant_api.auth.application.ports.output.PasswordResetTokenPersistencePort;
import com.nanoka.restaurant_api.auth.domain.model.PasswordResetToken;
import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetTokenService implements PasswordResetTokenServicePort {

    @Autowired
    private PasswordResetTokenPersistencePort persistencePort;

    @Autowired
    private UserServicePort userServicePort;

    @Override
    @Transactional
    public PasswordResetToken createToken(Long userId) {
        // Verificar si el usuario tiene un token existente
        Optional<PasswordResetToken> existingTokenOpt = persistencePort.findByUserId(userId);

        if (existingTokenOpt.isPresent()) {
            PasswordResetToken existingToken = existingTokenOpt.get();

            // Si el token tiene menos de 30 minutos de expiración, eliminamos el token antiguo
            if (existingToken.getExpirationDate().isBefore(LocalDateTime.now().plusMinutes(30))) {
                persistencePort.deleteByToken(existingToken.getToken());  // Eliminar el token antiguo
            } else {
                return existingToken;  // Si el token aún es válido, lo devolvemos
            }
        }

        // Generar un nuevo token
        String token = generateToken();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .userId(userId)
                .token(token)
                .expirationDate(LocalDateTime.now().plusHours(1))  // Duración del nuevo token
                .build();

        // Guardar y devolver el nuevo token
        return persistencePort.save(passwordResetToken);
    }

    @Override
    @Transactional
    public Optional<PasswordResetToken> validateToken(String token) {
        return persistencePort.findByToken(token)
                .filter(t -> t.getExpirationDate().isAfter(LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        persistencePort.findByToken(token)
                .ifPresent(t -> persistencePort.deleteByToken(token));
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> passwordResetToken = validateToken(token);
        if (passwordResetToken.isEmpty()) {
            throw new IllegalArgumentException("Token inválido o expirado.");
        }

        // Lógica para cambiar la contraseña en la base de datos.
        userServicePort.passwordReset(passwordResetToken.get().getUserId(), newPassword);

        // Borrar el token después de usarlo
        deleteToken(token);
    }

    private String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
