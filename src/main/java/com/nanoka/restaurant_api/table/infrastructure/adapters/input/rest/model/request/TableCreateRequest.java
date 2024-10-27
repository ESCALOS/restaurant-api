package com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableCreateRequest {
    @NotBlank(message = "El número de mesa es obligatorio")
    private String number;
}
