package com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByDocumentNumber(String documentNumber);
}
