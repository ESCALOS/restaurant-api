package com.nanoka.restaurant_api.serie.application.ports.input;

import java.util.List;

import com.nanoka.restaurant_api.serie.domain.model.Serie;

public interface SerieServicePort {
    Serie save(Serie serie);
    Serie update(Long id, Serie serie);
    Serie delete(Long id);
    Serie findById(Long id);
    List<Serie> findAll();

    /**
     * Update +1 correlative of serie
     * @param id
     * @return
     */
    Serie updateCorrelative(Long id);
}
