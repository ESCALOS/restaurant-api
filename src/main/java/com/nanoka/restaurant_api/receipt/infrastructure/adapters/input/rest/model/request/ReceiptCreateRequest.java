package com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptCreateRequest {
    @Size(min = 1, message = "Debe haber al menos un producto")
    private List<ReceiptDetailCreateRequest> details;

    @Positive(message = "El monto total debe ser mayor a 0")
    private Double totalAmount;

    private Long clientId;

    private Long userId;

    private Long orderId;

    private Long serieId;

    private int correlative;

    @NotBlank(message = "Falta el método de pago")
    private String paymentMethod;

    private String observation;
}
