package com.nanoka.restaurant_api.user.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
 private Long id;
 private String username;
 private String password;
 private String name;
 private DocumentTypeEnum documentType;
 private String documentNumber;
 private String phone;
 private Boolean isEnabled;
 private LocalDateTime createdAt;
 private LocalDateTime updatedAt;
 private Set<Role> roles;
}
