package com.nanoka.restaurant_api.user.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private Long id;
    private String name;
}
