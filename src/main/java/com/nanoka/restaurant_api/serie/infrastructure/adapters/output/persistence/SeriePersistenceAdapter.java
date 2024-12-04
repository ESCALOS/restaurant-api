package com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.nanoka.restaurant_api.serie.application.ports.output.SeriePersistencePort;
import com.nanoka.restaurant_api.serie.domain.model.Serie;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.mapper.SeriePersistenceMapper;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.repository.SerieRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeriePersistenceAdapter implements SeriePersistencePort {

    private final SerieRepository repository;
    private final SeriePersistenceMapper mapper;

    @Override
    public Optional<Serie> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toSerie);
    }

    @Override
    public Optional<Serie> findBySerie(String serie) {
        return repository.findBySerie(serie)
                .map(mapper::toSerie);
    }

    @Override
    public List<Serie> findAll() {
        return mapper.toSerieList(repository.findAll());
    }

    @Override
    public Serie save(Serie serie) {
        return mapper.toSerie(repository.save(mapper.toSerieEntity(serie)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
