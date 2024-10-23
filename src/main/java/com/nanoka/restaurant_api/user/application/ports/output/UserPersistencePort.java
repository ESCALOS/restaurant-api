package com.nanoka.restaurant_api.user.application.ports.output;

import com.nanoka.restaurant_api.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByDocumentNumber(String documentNumber);
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
}
