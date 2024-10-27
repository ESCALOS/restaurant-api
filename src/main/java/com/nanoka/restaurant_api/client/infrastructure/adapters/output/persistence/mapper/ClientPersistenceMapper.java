package com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.entity.ClientEntity;
import com.nanoka.restaurant_api.client.domain.model.Client;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientPersistenceMapper {
    ClientEntity toClientEntity(Client client);
    Client toClient(ClientEntity entity);
    List<Client> toClientList(List<ClientEntity> clientList);
}
