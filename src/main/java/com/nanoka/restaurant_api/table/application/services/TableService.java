package com.nanoka.restaurant_api.table.application.services;

import com.nanoka.restaurant_api.table.application.ports.input.TableServicePort;
import com.nanoka.restaurant_api.table.application.ports.output.TablePersistencePort;
import com.nanoka.restaurant_api.table.domain.model.Table;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService implements TableServicePort {

    private static final Logger logger = LoggerFactory.getLogger(TableService.class);
    private final TablePersistencePort persistencePort;

    @Override
    public Table findById(Long id) {
        logger.info("Buscando mesa con ID: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.TABLE_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Table> findAll() {
        logger.info("Buscando todas las mesas");
        return persistencePort.findAll();
    }

    @Override
    public Table save(Table table) {
        logger.info("Guardando nueva mesa con número: {}", table.getNumber());
        persistencePort.findByNumber(table.getNumber())
                .ifPresent(c -> {
                    logger.warn("Conflicto: La mesa con número {} ya existe", table.getNumber());
                    throw new ConflictException(ErrorCatelog.TABLE_ALREADY_EXIST.getMessage());
                });

        table.setIsAvailable(true);
        return persistencePort.save(table);
    }

    @Override
    public Table update(Long id, Table table) {
        logger.info("Actualizando mesa con ID: {}", id);
        return persistencePort.findById(id)
                .map(existingTable -> {
                    persistencePort.findByNumber(table.getNumber())
                            .filter(c -> !c.getId().equals(id))
                            .ifPresent(c -> {
                                logger.warn("Conflicto: La mesa con número {} ya existe", table.getNumber());
                                throw new ConflictException(ErrorCatelog.TABLE_ALREADY_EXIST.getMessage());
                            });
                    existingTable.setNumber(table.getNumber());
                    return persistencePort.save(existingTable);
                })
                .orElseThrow(() -> {
                    logger.error("No se encontró la mesa con ID: {}", id);
                    return new NotFoundException(ErrorCatelog.TABLE_NOT_FOUND.getMessage());
                });
    }

    @Override
    public void delete(Long id) {
        logger.info("Eliminando mesa con ID: {}", id);
        persistencePort.findById(id)
                .orElseThrow(() -> {
                    logger.error("No se encontró la mesa con ID: {}", id);
                    return new NotFoundException(ErrorCatelog.TABLE_NOT_FOUND.getMessage());
                });

        persistencePort.deleteById(id);
    }

    @Override
    public void toggleEnabled(Long id, Boolean isEnabled) {
        logger.info("Cambiando disponibilidad de la mesa con ID: {} a {}", id, isEnabled);
        Table table = this.findById(id);

        table.setIsAvailable(isEnabled);

        persistencePort.save(table);
    }
}
