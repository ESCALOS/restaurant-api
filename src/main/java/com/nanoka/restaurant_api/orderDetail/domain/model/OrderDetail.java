package com.nanoka.restaurant_api.orderDetail.domain.model;

import com.nanoka.restaurant_api.product.domain.model.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private Long id;
    private Long orderId;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
