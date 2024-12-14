package com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.nanoka.restaurant_api.serie.domain.model.Serie;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.model.request.SerieCreateRequest;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.model.response.SerieResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SerieRestMapper {
    Serie toSerie(SerieCreateRequest request);
    SerieResponse toSerieResponse(Serie serie);
    List<SerieResponse> toSerieResponseList(List<Serie> serieList);

}
