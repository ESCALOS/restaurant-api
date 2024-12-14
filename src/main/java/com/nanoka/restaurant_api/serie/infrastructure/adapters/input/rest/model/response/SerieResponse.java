package com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.serie.domain.model.EnumTypeVoucher;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SerieResponse {
    private Long id;
    private EnumTypeVoucher typeVoucher;
    private String serie;
    private int correlative;
    private boolean active;
}
