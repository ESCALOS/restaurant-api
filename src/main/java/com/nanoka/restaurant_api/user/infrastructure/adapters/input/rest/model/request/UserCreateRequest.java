package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request;

import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {


    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;

    @NotNull(message = "El tipo de documento es obligatorio")
    private DocumentTypeEnum documentType;

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    private String phone;

    @NotNull(message = "El rol es obligatorio")
    private String role;
}