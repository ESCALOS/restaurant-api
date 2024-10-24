package com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "Ingrese el precio")
    private BigDecimal price;

    private String imageUrl;

    @NotNull(message = "Ingrese el tipo")
    private Boolean isPrepared;

    @NotNull(message = "Ingrese la cantidad")
    private int stock;

    @NotNull(message = "El category_id es obligatorio")
    private Long categoryId;
}
