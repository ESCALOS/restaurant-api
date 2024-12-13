package com.nanoka.restaurant_api.orderDetail.infrastructure.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
