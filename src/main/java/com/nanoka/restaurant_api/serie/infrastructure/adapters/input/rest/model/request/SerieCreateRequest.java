package com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SerieCreateRequest {
    @NotBlank(message = "El tipo de comprobante es obligatorio")
    private String typeVoucher;
    @NotBlank(message = "La serie es obligatoria")
    private String serie;
    private int correlative;
    private boolean active;
}
