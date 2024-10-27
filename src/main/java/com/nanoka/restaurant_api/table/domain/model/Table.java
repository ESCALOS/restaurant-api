package com.nanoka.restaurant_api.table.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private Long id;
    private String number;
    private Boolean isAvailable = true;
}
