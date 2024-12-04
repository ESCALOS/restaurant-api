package com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.entity.SerieEntity;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<SerieEntity, Long> {
    Optional<SerieEntity> findBySerie(String serie);
}