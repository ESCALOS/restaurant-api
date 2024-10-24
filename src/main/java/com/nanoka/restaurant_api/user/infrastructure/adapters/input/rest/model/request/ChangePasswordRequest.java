package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Ingresa la contraseña actual")
    private String currentPassword;

    @NotBlank(message = "Ingresa la contraseña nueva")
    private String newPassword;
}
