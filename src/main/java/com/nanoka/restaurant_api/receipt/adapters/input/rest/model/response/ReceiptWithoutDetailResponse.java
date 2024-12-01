package com.nanoka.restaurant_api.receipt.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.client.domain.model.Client;
import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.response.OrderWithoutDetailResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptWithoutDetailResponse {
    private Long id;
    private UserResponse user;
    private OrderWithoutDetailResponse order;
    private Client client;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String observation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
