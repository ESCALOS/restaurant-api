package com.nanoka.restaurant_api.receipt.domain.model;

import com.nanoka.restaurant_api.client.domain.model.Client;
import com.nanoka.restaurant_api.order.domain.model.Order;
import com.nanoka.restaurant_api.receiptDetail.model.ReceiptDetail;
import com.nanoka.restaurant_api.user.domain.model.User;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.nanoka.restaurant_api.serie.domain.model.Serie;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private Long id;
    private Serie serie;
    private int correlative;
    private User user;
    private Client client;
    private Order order;
    private Double totalAmount;
    private List<ReceiptDetail> detailList;
    private String paymentMethod;
    private String observation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
