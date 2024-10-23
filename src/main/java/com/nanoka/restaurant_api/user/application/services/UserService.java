package com.nanoka.restaurant_api.user.application.services;

import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.application.ports.output.UserPersistencePort;
import com.nanoka.restaurant_api.user.domain.exception.UserNotFoundException;
import com.nanoka.restaurant_api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserPersistencePort persistencePort;

    @Override
    public User findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByUsername(String username) {
        return persistencePort.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public User save(User user) {
        return persistencePort.save(user);
    }

    @Override
    public User update(Long id, User user) {
       return persistencePort.findById(id)
               .map(saveUser -> {
                saveUser.setId(id);
                saveUser.setUsername(user.getUsername());
                saveUser.setPassword(user.getPassword());
                saveUser.setIsEnabled(user.getIsEnabled());
                return persistencePort.save(saveUser);
               })
               .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void delete(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new UserNotFoundException();
        }

        persistencePort.deleteById(id);
    }
}
