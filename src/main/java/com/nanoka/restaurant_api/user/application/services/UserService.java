package com.nanoka.restaurant_api.user.application.services;

import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.application.ports.output.UserPersistencePort;
import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.BadRequestException;
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
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
    }

    @Override
    public List<User> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public User save(User user) {
        if(persistencePort.findByUsername(user.getUsername()).isPresent()) {
            throw new BadCredentialsException(ErrorCatelog.USER_USERNAME_ALREADY_EXISTS.getMessage());
        }

        if(persistencePort.findByDocumentNumber(user.getDocumentNumber()).isPresent()) {
            throw new BadCredentialsException(ErrorCatelog.USER_DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage());
        }

        user.setPassword("password");
        user.setIsEnabled(true);

        return persistencePort.save(user);
    }

    @Override
    public User update(Long id, User user) {
        // Buscar el usuario existente para validar su existencia
        User existingUser = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage()));

        // Verificar si el username ya existe, omitiendo el usuario actual
        if (persistencePort.findByUsername(user.getUsername()).isPresent() &&
                !existingUser.getUsername().equals(user.getUsername())) {
            throw new BadRequestException(ErrorCatelog.USER_USERNAME_ALREADY_EXISTS.getMessage());
        }

        // Verificar si el documentNumber ya existe, omitiendo el usuario actual
        if (persistencePort.findByDocumentNumber(user.getDocumentNumber()).isPresent() &&
                !existingUser.getDocumentNumber().equals(user.getDocumentNumber())) {
            throw new BadRequestException(ErrorCatelog.USER_DOCUMENT_NUMBER_ALREADY_EXISTS.getMessage());
        }

        // Actualizar los campos permitidos del usuario
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setPhone(user.getPhone());
        existingUser.setDocumentType(user.getDocumentType());
        existingUser.setDocumentNumber(user.getDocumentNumber());
        existingUser.setRole(user.getRole());

        // Guardar el usuario actualizado
        return persistencePort.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        if (persistencePort.findById(id).isEmpty()) {
            throw new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage());
        }

        persistencePort.deleteById(id);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = persistencePort.findByUsername(username).orElseThrow(() -> new NotFoundException(ErrorCatelog.USER_NOT_FOUND.getMessage()));

        if(!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        user.setPassword(newPassword);
        persistencePort.save(user);
    }
}
