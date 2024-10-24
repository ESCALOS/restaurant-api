package com.nanoka.restaurant_api.category.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
}
