package com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.order.domain.model.StatusEnum;
import com.nanoka.restaurant_api.table.domain.model.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private UserResponse user;
    private Table table;
    private StatusEnum status;
    private BigDecimal totalAmount;
    private List<OrderDetailResponse> details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
