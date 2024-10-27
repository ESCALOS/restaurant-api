package com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.model.request;

import com.nanoka.restaurant_api.client.domain.model.DocumentTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientCreateRequest {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;
    @NotNull(message = "El tipo de documento es obligatorio")
    private DocumentTypeEnum documentType;
    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;
}
