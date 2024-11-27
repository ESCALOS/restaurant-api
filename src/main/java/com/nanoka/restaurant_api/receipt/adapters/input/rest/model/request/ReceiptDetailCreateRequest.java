package com.nanoka.restaurant_api.receipt.adapters.input.rest.model.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailCreateRequest {
    private Long orderDetailId;
    private int quantity;
}
