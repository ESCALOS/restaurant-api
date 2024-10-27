package com.nanoka.restaurant_api.client.application.services;

import com.nanoka.restaurant_api.client.application.ports.input.ClientServicePort;
import com.nanoka.restaurant_api.client.application.ports.output.ClientPersistencePort;
import com.nanoka.restaurant_api.client.domain.model.Client;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServicePort {

    private final ClientPersistencePort persistencePort;

    @Override
    public Client findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.CLIENT_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Client> findAll() { return persistencePort.findAll(); }

    @Override
    public Client save(Client client) {

        persistencePort.findByDocumentNumber(client.getDocumentNumber())
                .ifPresent(c -> {
                    throw new ConflictException(ErrorCatelog.CLIENT_DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage());
                });

        persistencePort.findByName(client.getName())
                .ifPresent(c -> {
                    throw new ConflictException(ErrorCatelog.CLIENT_ALREADY_EXIST.getMessage());
                });

        return persistencePort.save(client);
    }

    @Override
    public Client update(Long id, Client client) {
        return persistencePort.findById(id)
                .map(existingClient -> {
                    persistencePort.findByName(client.getName())
                            .filter(c -> !c.getId().equals(id))
                            .ifPresent(c -> {
                                throw new ConflictException(ErrorCatelog.CLIENT_ALREADY_EXIST.getMessage());
                            });
                    existingClient.setDocumentType(client.getDocumentType());
                    existingClient.setDocumentNumber(client.getDocumentNumber());
                    existingClient.setName(client.getName());
                    return persistencePort.save(existingClient);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.CLIENT_NOT_FOUND.getMessage()));
    }

    @Override
    public void delete(Long id) {
        persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.CLIENT_NOT_FOUND.getMessage()));

        persistencePort.deleteById(id);
    }
}
