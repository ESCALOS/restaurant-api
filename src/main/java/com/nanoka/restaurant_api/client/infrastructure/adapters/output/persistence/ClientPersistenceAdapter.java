package com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence;

import com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.mapper.ClientPersistenceMapper;
import com.nanoka.restaurant_api.client.infrastructure.adapters.output.persistence.repository.ClientRepository;
import com.nanoka.restaurant_api.client.application.ports.output.ClientPersistencePort;
import com.nanoka.restaurant_api.client.domain.model.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientPersistenceAdapter implements ClientPersistencePort {
    private final ClientRepository repository;
    private final ClientPersistenceMapper mapper;


    @Override
    public Optional<Client> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toClient);
    }

    @Override
    public Optional<Client> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber)
                .map(mapper::toClient);

    }

    @Override
    public Optional<Client> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toClient);
    }

    @Override
    public List<Client> findAll() {
        return mapper.toClientList(repository.findAll());
    }

    @Override
    public Client save(Client client) {
        return mapper.toClient(repository.save(mapper.toClientEntity(client)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
