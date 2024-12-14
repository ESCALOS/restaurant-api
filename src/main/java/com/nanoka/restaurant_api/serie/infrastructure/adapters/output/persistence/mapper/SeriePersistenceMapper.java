package com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.nanoka.restaurant_api.serie.domain.model.Serie;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.output.persistence.entity.SerieEntity;

@Mapper(componentModel = "spring")
public interface SeriePersistenceMapper {
    Serie toSerie(SerieEntity entity);
    SerieEntity toSerieEntity(Serie serie);
    List<Serie> toSerieList(List<SerieEntity> serieList);

}
