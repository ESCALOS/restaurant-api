package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request;

import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import com.nanoka.restaurant_api.user.domain.model.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {


    @NotBlank(message = "Field username cannot be empty or null")
    private String username;

    @NotBlank(message = "Field password cannot be empty or null")
    private String password;

    @NotBlank(message = "Field name cannot be empty or null")
    private String name;

    @NotNull(message = "Field documentType cannot be null")
    private DocumentTypeEnum documentType; // Usa DocumentTypeEnum en lugar de String

    @NotBlank(message = "Field documentNumber cannot be empty or null")
    private String documentNumber;

    @NotBlank(message = "Field phone cannot be empty or null")
    private String phone;

    @NotNull(message = "Field isEnabled cannot be null")
    private Boolean isEnabled;

    @NotNull(message = "Field roles cannot be empty")
    private List<@NotNull RoleEnum> roles; // Cambia List<String> a List<RoleEnum>
}