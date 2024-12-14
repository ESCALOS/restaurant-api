package com.nanoka.restaurant_api.orderDetail.aplication.ports.output;

import java.util.Optional;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;

public interface OrderDetailPersistencePort {
    Optional<OrderDetail> findById(Long id);
    OrderDetail save(OrderDetail orderDetail);
    void updateQuantityPaid(Long id, int quantityPaid);
}
