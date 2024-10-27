package com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.model.request.ClientCreateRequest;
import com.nanoka.restaurant_api.client.infrastructure.adapters.input.rest.model.response.ClientResponse;
import com.nanoka.restaurant_api.client.domain.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientRestMapper {
    Client toClient(ClientCreateRequest request);
    ClientResponse toClientResponse(Client client);
    List<ClientResponse> toClientResponseList(List<Client> clientList);
}
