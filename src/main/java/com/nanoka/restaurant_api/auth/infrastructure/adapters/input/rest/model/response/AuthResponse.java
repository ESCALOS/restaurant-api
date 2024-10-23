package com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthResponse(String username,
                           String message,
                           String jwt,
                           boolean status) {

}
