package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.mapper.UserRestMapper;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request.ChangePasswordRequest;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request.UserCreateRequest;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.response.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    private final UserServicePort servicePort;
    private final UserRestMapper restMapper;

    @GetMapping
    public List<UserResponse> findAll() {
        return restMapper.toUserResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable("id") Long id) {
        return restMapper.toUserResponse(servicePort.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toUserResponse(servicePort.save(restMapper.toUser(request))));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserCreateRequest request) {
        return restMapper.toUserResponse(servicePort.update(id, restMapper.toUser(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        servicePort.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        servicePort.changePassword(username, request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok("Contraseña cambiada exitosamente.");
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/enabled")
    public ResponseEntity<String> updateUserEnabledStatus(@PathVariable Long id, @RequestParam @NotNull Boolean isEnabled){
        servicePort.toggleEnabled(id,isEnabled);
        String message = isEnabled ? "Usuario habilitado exitosamente" : "Usuario deshabilitado exitosamente";
        return ResponseEntity.ok(message);
    }

}
