package com.nanoka.restaurant_api.client.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private Long id;
    private DocumentTypeEnum documentType;
    private String documentNumber;
    private String name;
}
