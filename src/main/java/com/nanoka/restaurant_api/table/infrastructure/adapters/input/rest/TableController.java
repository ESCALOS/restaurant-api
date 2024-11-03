package com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.mapper.TableRestMapper;
import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.request.TableCreateRequest;
import com.nanoka.restaurant_api.table.infrastructure.adapters.input.rest.model.response.TableResponse;
import com.nanoka.restaurant_api.table.application.ports.input.TableServicePort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tables")
@PreAuthorize("isAuthenticated()")
public class TableController {
    private final TableServicePort servicePort;
    private final TableRestMapper restMapper;

    @GetMapping
    public List<TableResponse> findAll() {
        return restMapper.toTableResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public TableResponse findById(@PathVariable("id") Long id) {
        return  restMapper.toTableResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TableResponse> save(@Valid @RequestBody TableCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toTableResponse(
                        servicePort.save(restMapper.toTable(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TableResponse update(@PathVariable Long id, @Valid @RequestBody TableCreateRequest request) {
        return restMapper.toTableResponse(
                servicePort.update(id, restMapper.toTable(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        servicePort.delete(id);
    }

    @PatchMapping("/{id}/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> enable(@PathVariable Long id, @RequestParam @NotNull Boolean isEnabled) {
        servicePort.toggleEnabled(id,isEnabled);
        String message = isEnabled ? "Mesaz habilitada exitosamente" : "Mesa deshabilitada exitosamente";
        return ResponseEntity.ok(message);
    }
}
