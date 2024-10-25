package com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.entity;

import com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence.entity.CategoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean isDish;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false,  name = "min_stock")
    private int minStock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}
