package com.nanoka.restaurant_api.orderDetail.aplication.ports.output;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;

import java.util.Optional;

public interface OrderDetailPersistencePort {
    Optional<OrderDetail> findById(Long id);
    OrderDetail save(OrderDetail orderDetail);
}
