package com.nanoka.restaurant_api.client.application.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nanoka.restaurant_api.client.application.ports.input.ClientServicePort;
import com.nanoka.restaurant_api.client.application.ports.output.ClientPersistencePort;
import com.nanoka.restaurant_api.client.domain.model.Client;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServicePort {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientPersistencePort persistencePort;

    @Override
    public Client findById(Long id) {
        logger.info("Buscando cliente con ID: {}", id);
        Client client = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.CLIENT_NOT_FOUND.getMessage()));
        logger.info("Cliente encontrado: {}", client);
        return client;
    }

    @Override
    public List<Client> findAll() {
        logger.info("Buscando todos los clientes");
        return persistencePort.findAll();
    }

    @Override
    public Client save(Client client) {
        logger.info("Guardando nuevo cliente: {}", client);
        persistencePort.findByDocumentNumber(client.getDocumentNumber())
                .ifPresent(c -> {
                    logger.error("El número de documento ya existe: {}", client.getDocumentNumber());
                    throw new ConflictException(ErrorCatelog.CLIENT_DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage());
                });

        persistencePort.findByName(client.getName())
                .ifPresent(c -> {
                    logger.error("El cliente ya existe: {}", client.getName());
                    throw new ConflictException(ErrorCatelog.CLIENT_ALREADY_EXIST.getMessage());
                });

        Client savedClient = persistencePort.save(client);
        logger.info("Cliente guardado: {}", savedClient);
        return savedClient;
    }

    @Override
    public Client update(Long id, Client client) {
        logger.info("Actualizando cliente con ID: {}", id);
        return persistencePort.findById(id)
                .map(existingClient -> {
                    persistencePort.findByName(client.getName())
                            .filter(c -> !c.getId().equals(id))
                            .ifPresent(c -> {
                                logger.error("El cliente ya existe: {}", client.getName());
                                throw new ConflictException(ErrorCatelog.CLIENT_ALREADY_EXIST.getMessage());
                            });
                    existingClient.setDocumentType(client.getDocumentType());
                    existingClient.setDocumentNumber(client.getDocumentNumber());
                    existingClient.setName(client.getName());
                    Client updatedClient = persistencePort.save(existingClient);
                    logger.info("Cliente actualizado: {}", updatedClient);
                    return updatedClient;
                })
                .orElseThrow(() -> {
                    logger.error("Cliente no encontrado con ID: {}", id);
                    return new NotFoundException(ErrorCatelog.CLIENT_NOT_FOUND.getMessage());
                });
    }

    @Override
    public void delete(Long id) {
        logger.info("Eliminando cliente con ID: {}", id);
        persistencePort.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cliente no encontrado con ID: {}", id);
                    return new NotFoundException(ErrorCatelog.CLIENT_NOT_FOUND.getMessage());
                });

        persistencePort.deleteById(id);
        logger.info("Cliente eliminado con ID: {}", id);
    }
}
