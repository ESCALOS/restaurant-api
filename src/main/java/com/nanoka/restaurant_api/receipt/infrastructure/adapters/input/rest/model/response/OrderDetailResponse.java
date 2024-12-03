package com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private ProductResponse product;
    private BigDecimal unitPrice;
    private int quantity;
}
