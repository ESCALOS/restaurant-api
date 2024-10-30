package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.response.UserResponse;
import com.nanoka.restaurant_api.productMovement.domain.model.MovementTypeEnum;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMovementResponse {
    private Long id;
    private ProductResponse product;
    private int quantity;
    private UserResponse user;
    private MovementTypeEnum movementType;
    private String reason;
}
