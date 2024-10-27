package com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tables")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String number;
    @Column(nullable = false, name = "is_available")
    private Boolean isAvailable = true;
}
