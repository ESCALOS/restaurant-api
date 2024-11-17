package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.mapper.UserRestMapper;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request.ChangePasswordRequest;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request.UserCreateRequest;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.response.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserServicePort servicePort;
    private final UserRestMapper restMapper;

    @GetMapping
    public List<UserResponse> findAll() {
        logger.info("Solicitud para obtener todos los usuarios");
        return restMapper.toUserResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable("id") Long id) {
        logger.info("Solicitud para obtener usuario con ID: {}", id);
        return restMapper.toUserResponse(servicePort.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserCreateRequest request) {
        logger.info("Solicitud para crear un nuevo usuario");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toUserResponse(servicePort.save(restMapper.toUser(request))));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserCreateRequest request) {
        logger.info("Solicitud para actualizar usuario con ID: {}", id);
        return restMapper.toUserResponse(servicePort.update(id, restMapper.toUser(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Solicitud para eliminar usuario con ID: {}", id);
        servicePort.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Solicitud para cambiar contraseña del usuario: {}", username);
        servicePort.changePassword(username, request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok("Contraseña cambiada exitosamente.");
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/enabled")
    public ResponseEntity<String> updateUserEnabledStatus(@PathVariable Long id, @RequestParam @NotNull Boolean isEnabled){
        logger.info("Solicitud para {} usuario con ID: {}", isEnabled ? "habilitar" : "deshabilitar", id);
        servicePort.toggleEnabled(id,isEnabled);
        String message = isEnabled ? "Usuario habilitado exitosamente" : "Usuario deshabilitado exitosamente";
        return ResponseEntity.ok(message);
    }

}
