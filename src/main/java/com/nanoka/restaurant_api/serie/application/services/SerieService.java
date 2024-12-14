package com.nanoka.restaurant_api.serie.application.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nanoka.restaurant_api.category.application.services.CategoryService;
import com.nanoka.restaurant_api.serie.application.ports.input.SerieServicePort;
import com.nanoka.restaurant_api.serie.application.ports.output.SeriePersistencePort;
import com.nanoka.restaurant_api.serie.domain.model.Serie;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SerieService implements SerieServicePort {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final SeriePersistencePort persistencePort;

    @Override
    public Serie findById(Long id) {
        logger.info("Finding serie by id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> {
                    logger.error("Serie not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.SERIE_NOT_FOUND.getMessage());
                });
    }

    @Override
    public List<Serie> findAll() {
        logger.info("Finding all series");
        return persistencePort.findAll();
    }

    @Override
    public Serie save(Serie serie) {
        logger.info("Saving serie: {}", serie.getSerie());
        persistencePort.findBySerie(serie.getSerie())
                .ifPresent(c -> {
                    logger.error("Serie already exists with name: {}", serie.getSerie());
                    throw new ConflictException(ErrorCatelog.SERIE_ALREADY_EXIST.getMessage());
                });

        return persistencePort.save(serie);
    }

    @Override
    public Serie update(Long id, Serie serie) {
        logger.info("Updating serie with id: {}", id);
        return persistencePort.findById(id)
                .map(existingSerie -> {
                    persistencePort.findBySerie(serie.getSerie())
                            .filter(c -> !c.getId().equals(id))
                            .ifPresent(c -> {
                                logger.error("Serie already exists with name: {}", serie.getSerie());
                                throw new ConflictException(ErrorCatelog.SERIE_ALREADY_EXIST.getMessage());
                            });

                    existingSerie.setSerie(serie.getSerie());
                    existingSerie.setCorrelative(serie.getCorrelative());
                    existingSerie.setActive(serie.isActive());

                    return persistencePort.save(existingSerie);
                })
                .orElseThrow(() -> {
                    logger.error("Serie not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.SERIE_NOT_FOUND.getMessage());
                });
    }

    @Override
    public Serie delete(Long id) {
        logger.info("Deleting serie with id: {}", id);
        Serie serie = persistencePort.findById(id)
                .orElseThrow(() -> {
                    logger.error("Serie not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.SERIE_NOT_FOUND.getMessage());
                });
        persistencePort.deleteById(id);
        return serie;
    }

    @Override
    public Serie updateCorrelative(Long id) {
        logger.info("Updating correlative of serie with id: {}", id);
        return persistencePort.findById(id)
                .map(serie -> {
                    serie.setCorrelative(serie.getCorrelative() + 1);
                    return persistencePort.save(serie);
                })
                .orElseThrow(() -> {
                    logger.error("Serie not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.SERIE_NOT_FOUND.getMessage());
                });

    }

    @Override
    public int getCorrelative(Long id) {
        logger.info("Getting correlative of serie with id: {}", id);
        return persistencePort.findById(id)
                .map(Serie::getCorrelative)
                .orElseThrow(() -> {
                    logger.error("Serie not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.SERIE_NOT_FOUND.getMessage());
                });
    }
}
