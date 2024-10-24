package com.nanoka.restaurant_api.product.domain.model;

import com.nanoka.restaurant_api.category.domain.model.Category;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isPrepared;
    private int stock;
    private Category category;

}
