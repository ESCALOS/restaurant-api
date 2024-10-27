package com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {
    private Long id;
    private String number;
    private Boolean isAvailable;
}
