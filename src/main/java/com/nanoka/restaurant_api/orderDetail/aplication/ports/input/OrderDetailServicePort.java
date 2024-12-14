package com.nanoka.restaurant_api.orderDetail.aplication.ports.input;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;

import java.util.Optional;

public interface OrderDetailServicePort {
    OrderDetail findById(Long id);
    OrderDetail save(OrderDetail orderDetail);
    OrderDetail update(Long id, OrderDetail orderDetail);

    /**
     * Update quantityPaid of orderDetail
     * @param id
     */
    void updateQuantityPaid(Long id, int quantityPaid);
}
