package com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.mapper.ClientRestMapper;
import com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.model.request.ClientCreateRequest;
import com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.model.response.ClientResponse;
import com.nanoka.restaurant_api.client.application.ports.input.ClientServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clients")
@PreAuthorize("isAuthenticated()")
public class ClientController {
    private final ClientServicePort servicePort;
    private final ClientRestMapper restMapper;

    @GetMapping
    public List<ClientResponse> findAll() {
        return restMapper.toClientResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public ClientResponse findById(@PathVariable("id") Long id) {
        return  restMapper.toClientResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponse> save(@Valid @RequestBody ClientCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toClientResponse(
                        servicePort.save(restMapper.toClient(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientCreateRequest request) {
        return restMapper.toClientResponse(
                servicePort.update(id, restMapper.toClient(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        servicePort.delete(id);
    }
}
