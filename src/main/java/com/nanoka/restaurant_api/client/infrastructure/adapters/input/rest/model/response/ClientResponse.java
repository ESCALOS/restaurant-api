package com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.model.response;

import com.nanoka.restaurant_api.client.domain.model.DocumentTypeEnum;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private DocumentTypeEnum documentType;
    private String documentNumber;
    private String name;
}
