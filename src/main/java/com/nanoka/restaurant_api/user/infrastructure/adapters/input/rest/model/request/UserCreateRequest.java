package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request;

import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {


    @NotBlank(message = "Field username cannot be empty or null")
    private String username;

    @NotBlank(message = "Field name cannot be empty or null")
    private String name;

    @NotNull(message = "Field documentType cannot be null")
    private DocumentTypeEnum documentType; // Usa DocumentTypeEnum en lugar de String

    @NotBlank(message = "Field documentNumber cannot be empty or null")
    private String documentNumber;

    @NotBlank(message = "Field phone cannot be empty or null")
    private String phone;

    @NotNull(message = "Field roles cannot be empty")
    private String role;
}