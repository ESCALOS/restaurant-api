package com.nanoka.restaurant_api.receipt.adapters.input.rest.model.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetailResponse {
    private Long id;
    private OrderDetailResponse orderDetail;
    private BigDecimal unitPrice;
    private int quantity;
}
