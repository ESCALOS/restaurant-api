package com.nanoka.restaurant_api.orderDetail.infrastructure.repositories;

import com.nanoka.restaurant_api.orderDetail.persistence.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    Optional<OrderDetailEntity> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE OrderDetailEntity od SET od.quantityPaid = :quantityPaid WHERE od.id = :id")
    void updateQuantityPaid(@Param("id") Long id, @Param("quantityPaid") int quantityPaid);
}
