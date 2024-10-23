package com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String username,
                           @NotBlank String password) {
}
