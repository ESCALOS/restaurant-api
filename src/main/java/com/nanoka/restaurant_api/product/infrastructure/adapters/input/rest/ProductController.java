package com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.product.application.services.ProductService;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.mapper.ProductRestMapper;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.request.ProductCreateRequest;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@PreAuthorize("isAuthenticated()")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductServicePort servicePort;
    private final ProductRestMapper restMapper;

    @GetMapping
    public List<ProductResponse> findAll() {
        logger.info("Obteniendo todos los productos");
        return restMapper.toProductResponseList(servicePort.findAll(false));
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable("id") Long id) {
        logger.info("Obteniendo producto con id: {}", id);
        return restMapper.toProductResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductCreateRequest request) {
        logger.info("Guardando nuevo producto");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toProductResponse(
                        servicePort.save(restMapper.toProduct(request),false)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest request) {
        logger.info("Actualizando producto con id: {}", id);
        return restMapper.toProductResponse(
                servicePort.update(id, restMapper.toProduct(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        logger.info("Eliminando producto con id: {}", id);
        servicePort.delete(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse modifyStock(@PathVariable Long id, @RequestParam int quantity){
        logger.info("Modificando stock del producto con id: {}", id);
        return restMapper.toProductResponse(servicePort.modifyStock(id,quantity));
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        logger.info("Exportando productos a Excel");
        byte[] excelData = servicePort.exportProductsToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "productos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
