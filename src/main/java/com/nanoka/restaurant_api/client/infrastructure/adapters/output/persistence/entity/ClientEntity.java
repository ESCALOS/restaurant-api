package com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.entity;

import com.nanoka.restaurant_api.client.domain.model.DocumentTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "document_type")
    private DocumentTypeEnum documentType;
    @Column(nullable = false, name = "document_number")
    private String documentNumber;
    @Column(nullable = false, unique = true)
    private String name;
}
