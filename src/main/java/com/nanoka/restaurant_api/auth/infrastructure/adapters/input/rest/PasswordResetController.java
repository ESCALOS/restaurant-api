package com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.auth.application.services.PasswordResetTokenService;
import com.nanoka.restaurant_api.auth.domain.model.PasswordResetToken;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.request.PasswordResetRequest;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.request.PasswordResetResetRequest;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.response.PasswordResetResponse;
import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.domain.model.User;
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
    private final UserServicePort userService;

    // Endpoint para crear un token de restablecimiento
    @PostMapping
    public ResponseEntity<PasswordResetResponse> createResetToken(@RequestBody @Valid PasswordResetRequest request) {
        // Suponiendo que `userId` se obtiene de alguna forma, como del contexto de seguridad
        User user = userService.findByEmail(request.getEmail());
        PasswordResetToken token = passwordResetTokenService.createToken(user.getId());

        PasswordResetResponse response = new PasswordResetResponse(token.getToken());
        return new ResponseEntity<>(response, HttpStatus.OK);
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
