package com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.receipt.domain.model.Receipt;
import com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.mapper.ReceiptRestMapper;
import com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.request.ReceiptCreateRequest;
import com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.response.ReceiptResponse;
import com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.response.ReceiptWithoutDetailResponse;
import com.nanoka.restaurant_api.receipt.application.ports.input.ReceiptServicePort;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipts")
@PreAuthorize("isAuthenticated()")
public class ReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);
    private final ReceiptServicePort servicePort;
    private final ReceiptRestMapper restMapper;

    @GetMapping
    public List<ReceiptWithoutDetailResponse> findAll() {
        logger.info("Obteniendo todos los recibos");
        return restMapper.toReceiptResponseListWithoutDetails(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public ReceiptResponse findById(@PathVariable("id") Long id) {
        logger.info("Obteniendo recibo con id: {}", id);
        return restMapper.toReceiptResponse(servicePort.findById(id));
    }

    //funcion que recibe un ReceiptCreateRequest y devuelve un ReceiptResponse
    @PostMapping
    public ResponseEntity<ReceiptResponse> save(@RequestBody ReceiptCreateRequest request) {
        logger.info("Creando recibo");
        return ResponseEntity.status(201)
                .body(restMapper.toReceiptResponse(
                        servicePort.save(request)));
    }
    

}
