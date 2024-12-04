package com.nanoka.restaurant_api.serie.application.ports.output;

import java.util.List;
import java.util.Optional;

import com.nanoka.restaurant_api.serie.domain.model.Serie;

public interface SeriePersistencePort {
    Optional<Serie> findById(Long id);
    Optional<Serie> findBySerie(String serie);
    List<Serie> findAll();
    Serie save(Serie serie);
    void deleteById(Long id);
}
