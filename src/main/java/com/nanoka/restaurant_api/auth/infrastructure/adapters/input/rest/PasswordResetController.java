package com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.auth.application.services.PasswordResetTokenService;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.request.PasswordResetRequest;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.request.PasswordResetResetRequest;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/password-reset")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class PasswordResetController {
    private final PasswordResetTokenService passwordResetTokenService;

    // Endpoint para crear un token de restablecimiento
    @PostMapping
    public ResponseEntity<?> createResetToken(@RequestBody @Valid PasswordResetRequest request) throws MessagingException {
        passwordResetTokenService.createToken(request.getEmail());
        return ResponseEntity.ok("Correo Enviado");
    }

    // Endpoint para validar el token
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        boolean isValid = passwordResetTokenService.validateToken(token).isPresent();
        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Token inválido o expirado.");
        }
    }

    // Endpoint para restablecer la contraseña
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetResetRequest request) {
        try {
            passwordResetTokenService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
