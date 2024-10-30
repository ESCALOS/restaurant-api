package com.nanoka.restaurant_api.order.application.ports.input;

import com.nanoka.restaurant_api.order.domain.model.Order;
import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.request.OrderCreateRequest;

import java.util.List;

public interface OrderServicePort {
    Order findById(Long id);
    List<Order> findAll();
    Order save(Long tableId, OrderCreateRequest orderCreateRequest);
    void delete(Long id);
    Order addProductToOrder(Long orderId, Long productId, int quantity);
    Order updateProductQuantity(Long orderId, Long productId, int quantity);
    void removeProductFromOrder(Long orderId, Long productId);
}
