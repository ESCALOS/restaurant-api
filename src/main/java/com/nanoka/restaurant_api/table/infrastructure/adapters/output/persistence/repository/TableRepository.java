package com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
    Optional<TableEntity> findByNumber(String number);
}
