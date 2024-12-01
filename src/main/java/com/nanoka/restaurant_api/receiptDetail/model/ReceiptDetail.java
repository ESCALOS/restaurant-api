package com.nanoka.restaurant_api.receiptDetail.model;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetail {
    private Long id;
    private OrderDetail orderDetail;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
