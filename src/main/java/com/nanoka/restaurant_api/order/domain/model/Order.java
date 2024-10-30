package com.nanoka.restaurant_api.order.domain.model;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.table.domain.model.Table;
import com.nanoka.restaurant_api.user.domain.model.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private User user;
    private Table table;
    private StatusEnum status;
    private BigDecimal totalAmount;
    private List<OrderDetail> detailList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
