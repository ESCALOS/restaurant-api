package com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.entity.OrderEntity;
import com.nanoka.restaurant_api.order.domain.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {
    OrderEntity toOrderEntity(Order order);
    Order toOrder(OrderEntity entity);
    List<Order> toOrderList(List<OrderEntity> orderList);
}
