package com.nanoka.restaurant_api.client.application.ports.input;

import com.nanoka.restaurant_api.client.domain.model.Client;

import java.util.List;

public interface ClientServicePort {
    Client findById(Long id);
    List<Client> findAll();
    Client save(Client client);
    Client update(Long id, Client client);
    void delete(Long id);
}
