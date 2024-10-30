package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.output.persistence.entity;

import com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.entity.OrderEntity;
import com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.entity.ProductEntity;
import com.nanoka.restaurant_api.productMovement.domain.model.MovementTypeEnum;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_movements")
public class ProductMovementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementTypeEnum movementType;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
