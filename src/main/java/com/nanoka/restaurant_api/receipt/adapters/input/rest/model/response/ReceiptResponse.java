package com.nanoka.restaurant_api.receipt.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.client.domain.model.Client;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponse {
    private Long id;
    private UserResponse user;
    private Client client;
    private List<ReceiptDetailResponse> details;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
