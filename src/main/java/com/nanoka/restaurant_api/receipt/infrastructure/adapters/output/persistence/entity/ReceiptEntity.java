package com.nanoka.restaurant_api.receipt.infrastructure.adapters.output.persistence.entity;

import com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.entity.ClientEntity;
import com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.entity.OrderEntity;
import com.nanoka.restaurant_api.receiptDetail.entity.ReceiptDetailEntity;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.UserEntity;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.entity.SerieEntity;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "receipts")
public class ReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "serie_id", nullable = false)
    private SerieEntity serie;

    @Column(nullable = false)
    private int correlative;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "receipt_id")
    private List<ReceiptDetailEntity> detailList = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String paymentMethod;

    private String observation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
