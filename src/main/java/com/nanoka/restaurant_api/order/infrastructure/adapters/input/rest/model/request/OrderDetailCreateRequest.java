package com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailCreateRequest {
    private Long productId;
    private int quantity;
}
