package com.nanoka.restaurant_api.client.application.ports.output;

import com.nanoka.restaurant_api.client.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientPersistencePort {
    Optional<Client> findById(Long id);
    Optional<Client> findByDocumentNumber(String documentNumber);
    Optional<Client> findByName(String name);
    List<Client> findAll();
    Client save(Client client);
    void deleteById(Long id);
}
