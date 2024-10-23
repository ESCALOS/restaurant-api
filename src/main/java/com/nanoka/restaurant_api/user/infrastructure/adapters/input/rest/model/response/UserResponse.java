package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import com.nanoka.restaurant_api.user.domain.model.RoleEnum;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private DocumentTypeEnum documentType;
    private String documentNumber;
    private String phone;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private RoleEnum role;
}
