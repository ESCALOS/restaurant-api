package com.nanoka.restaurant_api.auth.domain.model;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    private Long id;
    private String token;
    private Long userId;
    private LocalDateTime expirationDate;
}
