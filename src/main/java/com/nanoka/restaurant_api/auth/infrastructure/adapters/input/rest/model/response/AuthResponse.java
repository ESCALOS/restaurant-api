package com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "token", "status"})
public record AuthResponse(String username,
                           String message,
                           String token,
                           boolean status) {

}
