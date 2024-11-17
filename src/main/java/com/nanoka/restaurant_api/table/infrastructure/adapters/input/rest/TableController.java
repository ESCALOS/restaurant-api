package com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.mapper.TableRestMapper;
import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.request.TableCreateRequest;
import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.response.TableResponse;
import com.nanoka.restaurant_api.table.application.ports.input.TableServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tables")
@PreAuthorize("isAuthenticated()")
public class TableController {
    private static final Logger logger = LoggerFactory.getLogger(TableController.class);
    private final TableServicePort servicePort;
    private final TableRestMapper restMapper;

    @GetMapping
    public List<TableResponse> findAll() {
        logger.info("Solicitud para obtener todas las mesas");
        return restMapper.toTableResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public TableResponse findById(@PathVariable("id") Long id) {
        logger.info("Solicitud para obtener la mesa con ID: {}", id);
        return  restMapper.toTableResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TableResponse> save(@Valid @RequestBody TableCreateRequest request) {
        logger.info("Solicitud para guardar una nueva mesa");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toTableResponse(
                        servicePort.save(restMapper.toTable(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TableResponse update(@PathVariable Long id, @Valid @RequestBody TableCreateRequest request) {
        logger.info("Solicitud para actualizar la mesa con ID: {}", id);
        return restMapper.toTableResponse(
                servicePort.update(id, restMapper.toTable(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        logger.info("Solicitud para eliminar la mesa con ID: {}", id);
        servicePort.delete(id);
    }
}
