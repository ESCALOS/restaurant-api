package com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanoka.restaurant_api.auth.application.services.AuthService;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.request.AuthLoginRequest;
import com.nanoka.restaurant_api.auth.infrastructure.adapters.input.rest.model.response.AuthResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        logger.info("Intento de inicio de sesión para el usuario: {}", userRequest.username());
        return new ResponseEntity<>(this.authService.loginUser(userRequest), HttpStatus.OK);
    }

}
