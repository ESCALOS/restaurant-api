package com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.request.TableCreateRequest;
import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.response.TableResponse;
import com.nanoka.restaurant_api.table.domain.model.Table;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TableRestMapper {
    Table toTable(TableCreateRequest request);
    TableResponse toTableResponse(Table table);
    List<TableResponse> toTableResponseList(List<Table> tableList);
}
