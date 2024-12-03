package com.nanoka.restaurant_api.serie.application.ports.input;

import com.nanoka.restaurant_api.serie.domain.model.Serie;

import java.util.List;

public class SerieServicePort {
    Serie createSerie(Serie serie);
    Serie findById(Long id);
    List<Serie> findAll();
    void desactiveSerie(Long id);

    /**
     * Obtiene y actualiza el siguiente correlativo de una serie.
     *
     * @param serieCode Código único de la serie.
     * @return El siguiente número de correlativo.
     */
    int getNextCorrelative(String serieCode);

}
