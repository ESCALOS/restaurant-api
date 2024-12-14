package com.nanoka.restaurant_api.orderDetail.domain.model;

import com.nanoka.restaurant_api.product.domain.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    private int quantityPaid;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
