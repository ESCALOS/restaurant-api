package com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence.entity.TableEntity;
import com.nanoka.restaurant_api.table.domain.model.Table;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TablePersistenceMapper {
    TableEntity toTableEntity(Table table);
    Table toTable(TableEntity entity);
    List<Table> toTableList(List<TableEntity> tableList);
}
