package com.nanoka.restaurant_api.order.application.ports.output;

import com.nanoka.restaurant_api.order.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderPersistencePort {
    Optional<Order> findById(Long id);
    List<Order> findAll();
    Order save(Order category);
    void deleteById(Long id);
}
