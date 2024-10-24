package com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.category.domain.model.Category;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isDish;
    private int stock;
    private Category category;
}
