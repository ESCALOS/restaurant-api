package com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> { }
