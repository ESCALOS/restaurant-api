package com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByPaidFalseAndTableId(Long tableId);
}
