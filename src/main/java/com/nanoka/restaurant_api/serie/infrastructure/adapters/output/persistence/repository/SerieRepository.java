package com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.entity.SerieEntity;

import java.util.Optional;

@Repository
public interface SerieRepository extends JpaRepository<SerieEntity, Long> {
    Optional<SerieEntity> findBySerie(String serie);

    @Query("SELECT s.correlative FROM SerieEntity s WHERE s.id = :id")
    int getCorrelative(Long id);
}