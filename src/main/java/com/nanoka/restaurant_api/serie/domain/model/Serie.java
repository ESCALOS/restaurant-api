package com.nanoka.restaurant_api.serie.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Serie {
    private Long id;
    private EnumTypeVoucher typeVoucher;
    private String serie;
    private int correlative;
    private boolean active;
}
