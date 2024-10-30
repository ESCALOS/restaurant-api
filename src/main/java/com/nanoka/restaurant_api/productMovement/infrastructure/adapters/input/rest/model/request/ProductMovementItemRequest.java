package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.request;

import com.nanoka.restaurant_api.productMovement.domain.model.MovementTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMovementItemRequest {
    @NotNull(message = "El producto es obligatorio")
    private Long productId;
    @NotNull(message = "La cantidad es obligatoria")
    private int quantity;
    @NotNull(message = "Indique el tipo de movimiento: INPUT/OUTPUT")
    private MovementTypeEnum movementType;
    @NotBlank(message = "Indique la razón del movimiento")
    private String reason;
}
