package com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByDocumentNumber(String documentNumber);
    Optional<ClientEntity> findByName(String name);
}
