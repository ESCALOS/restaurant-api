package com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.CategoryController;
import com.nanoka.restaurant_api.serie.application.ports.input.SerieServicePort;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.mapper.SerieRestMapper;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.model.request.SerieCreateRequest;
import com.nanoka.restaurant_api.serie.infrastructure.adapters.input.rest.model.response.SerieResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/series")
@PreAuthorize("isAuthenticated()")
public class SerieController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final SerieServicePort servicePort;
    private final SerieRestMapper restMapper;

    @GetMapping
    public List<SerieResponse> findAll() {
        logger.info("Fetching all series");
        return restMapper.toSerieResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public SerieResponse findById(@PathVariable("id") Long id) {
        logger.info("Fetching serie by id: {}", id);
        return restMapper.toSerieResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SerieResponse> save(@Valid @RequestBody SerieCreateRequest request) {
        logger.info("Saving new serie: {}", request.getSerie());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toSerieResponse(
                        servicePort.save(restMapper.toSerie(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public SerieResponse update(@PathVariable Long id, @Valid @RequestBody SerieCreateRequest request) {
        logger.info("Updating serie by id: {}", id);
        return restMapper.toSerieResponse(servicePort.update(id, restMapper.toSerie(request)));
    }

    @PatchMapping("/{id}/update-correlative")
    @PreAuthorize("hasRole('ADMIN')")
    public SerieResponse updateCorrelative(@PathVariable Long id) {
        logger.info("Updating correlative of serie by id: {}", id);
        return restMapper.toSerieResponse(servicePort.updateCorrelative(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting serie by id: {}", id);
        //return restMapper.toSerieResponse(servicePort.delete(id));
        servicePort.delete(id);
    }

}
