package com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "series")
public class SerieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_voucher")
    private String typeVoucher;

    @Column(name = "serie")
    private String serie;

    @Column(name = "correlative")
    private int correlative;

    @Column(name = "active")
    private boolean active;
}
