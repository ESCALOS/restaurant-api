package com.nanoka.restaurant_api.productMovement.domain.model;

import com.nanoka.restaurant_api.order.domain.model.Order;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.user.domain.model.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductMovement {
    private Long id;
    private User user;
    private Product product;
    private int quantity;
    private MovementTypeEnum movementType;
    private String reason;
    private Order order;
}
