package com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.table.domain.model.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithoutDetailResponse {
    private Long id;
    private UserResponse user;
    private Table table;
    private Boolean paid;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
