package com.nanoka.restaurant_api.receipt.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
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

    private Long clientId;

    @NotBlank(message = "Falta el método de pago")
    private String paymentMethod;

    private String observation;
}
