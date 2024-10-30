package com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    @Size(min = 1, message = "Debe haber al menos un producto")
    private List<OrderDetailCreateRequest> details;
}
