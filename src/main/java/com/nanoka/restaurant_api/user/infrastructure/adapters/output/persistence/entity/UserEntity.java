package com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity;

import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import com.nanoka.restaurant_api.user.domain.model.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "document_type")
    private DocumentTypeEnum documentType;

    @Column(nullable = false, name = "document_number")
    private String documentNumber;

    private String phone;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

}
