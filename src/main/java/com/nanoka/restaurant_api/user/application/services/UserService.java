package com.nanoka.restaurant_api.user.application.services;

import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.application.ports.output.UserPersistencePort;
import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserPersistencePort persistencePort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage()));
    }

    @Override
    public User findByUsername(String username) {
        return persistencePort.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage()));
    }

    @Override
    public List<User> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public User save(User user) {
        // Verificar que el nombre de usuario sea único
        persistencePort.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new ConflictException(ErrorCatelog.USER_USERNAME_ALREADY_EXISTS.getMessage());
        });

        // Verificar que el número de documento sea único
        persistencePort.findByDocumentNumber(user.getDocumentNumber()).ifPresent(u -> {
            throw new ConflictException(ErrorCatelog.USER_DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage());
        });

        // Colocar los datos por defecto
        user.setPassword(passwordEncoder.encode("password"));
        user.setIsEnabled(true);

        // Persistir los datos en la BD
        return persistencePort.save(user);
    }

    @Override
    public User update(Long id, User user) {
        // Obtener al usuario
        User existingUser = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage()));

        // Verificar que el nombre de usuario sea único
        persistencePort.findByUsername(user.getUsername())
                .filter(u -> !u.getUsername().equals(existingUser.getUsername()))
                .ifPresent(u -> { throw new ConflictException(ErrorCatelog.USER_USERNAME_ALREADY_EXISTS.getMessage()); });

        // Verificar que el número de documento sea único
        persistencePort.findByDocumentNumber(user.getDocumentNumber())
                .filter(u -> !u.getDocumentNumber().equals(existingUser.getDocumentNumber()))
                .ifPresent(u -> { throw new ConflictException(ErrorCatelog.USER_DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage()); });

        // Guardar datos en memoria
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setPhone(user.getPhone());
        existingUser.setDocumentType(user.getDocumentType());
        existingUser.setDocumentNumber(user.getDocumentNumber());
        existingUser.setRole(user.getRole());

        // Persistir los datos en la BD
        return persistencePort.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        // Verificar si el usuario existe
        persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage()));

        persistencePort.deleteById(id);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        persistencePort.findByUsername(username)
                .filter(user -> passwordEncoder.matches(currentPassword, user.getPassword()))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return persistencePort.save(user);
                })
                .orElseThrow(() -> new BadCredentialsException(ErrorCatelog.BAD_CREDENTIALS.getMessage()));
    }

    @Override
    public void toggleEnabled(Long id, Boolean isEnabled) {
        User user = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage()));

        user.setIsEnabled(isEnabled);

        persistencePort.save(user);
    }
}
