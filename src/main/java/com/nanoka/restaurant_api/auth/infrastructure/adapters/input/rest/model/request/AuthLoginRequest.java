package com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank(message = "Falta el nombre de usuario") String username,
                           @NotBlank(message = "Falta la contraseña") String password) {
}
