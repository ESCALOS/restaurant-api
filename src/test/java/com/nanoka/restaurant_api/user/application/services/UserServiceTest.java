package com.nanoka.restaurant_api.user.application.services;

import com.nanoka.restaurant_api.user.application.ports.output.UserPersistencePort;
import com.nanoka.restaurant_api.user.domain.model.DocumentTypeEnum;
import com.nanoka.restaurant_api.user.domain.model.RoleEnum;
import com.nanoka.restaurant_api.user.domain.model.User;

import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserPersistencePort persistencePort;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSaveUserSuccessfully() {
        User user = new User(null, "carlos_escate", "password", "Carlos Escate", DocumentTypeEnum.DNI, "123456789", "1234567890", true, null, null, RoleEnum.WAITER);

        when(persistencePort.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(persistencePort.findByDocumentNumber(user.getDocumentNumber())).thenReturn(Optional.empty());
        when(persistencePort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("carlos_escate", savedUser.getUsername());
        verify(persistencePort).findByUsername(user.getUsername());
        verify(persistencePort).findByDocumentNumber(user.getDocumentNumber());
        verify(persistencePort).save(any(User.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenUsernameAlreadyExists() {
        User user = new User(null, "carlos_escate", "password", "Carlos Escate", DocumentTypeEnum.DNI, "123456789", "1234567890", true, null, null, RoleEnum.WAITER);
        when(persistencePort.findByUsername("carlos_escate")).thenReturn(Optional.of(user));

        ConflictException exception = assertThrows(ConflictException.class, () -> userService.save(user));
        assertEquals(ErrorCatelog.USER_USERNAME_ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(persistencePort).findByUsername("carlos_escate");
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        Long id = 1L;
        User existingUser = new User(id, "carlosescate", "password", "Carlos Escate", DocumentTypeEnum.DNI, "123456789", "1234567890", true, null, null, RoleEnum.WAITER);
        User updatedUser = new User(id, "carlosmendoza", "newpassword", "Carlos Mendoza", DocumentTypeEnum.DNI, "987654321", "0987654321", true, null, null, RoleEnum.STOREKEEPER);

        when(persistencePort.findById(id)).thenReturn(Optional.of(existingUser));
        when(persistencePort.findByUsername(updatedUser.getUsername())).thenReturn(Optional.empty());
        when(persistencePort.findByDocumentNumber(updatedUser.getDocumentNumber())).thenReturn(Optional.empty());
        when(persistencePort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update(id, updatedUser);

        assertEquals("carlosmendoza", result.getUsername());
        assertEquals(id, result.getId());
        verify(persistencePort).findById(id);
        verify(persistencePort).save(any(User.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        Long id = 1L;
        User existingUser = new User(id, "carlosescate", "password", "Carlos Escate", DocumentTypeEnum.DNI, "123456789", "1234567890", true, null, null, RoleEnum.STOREKEEPER);

        when(persistencePort.findById(id)).thenReturn(Optional.of(existingUser));

        // Ejecutar el método de eliminación en el servicio
        userService.delete(id);

        verify(persistencePort).findById(id);
        verify(persistencePort).deleteById(id);
    }

    @Test
    void shouldToggleUserEnabledStatusSuccessfully() {
        Long id = 1L;
        User user = new User(null, "carlos_escate", "password", "Carlos Escate", DocumentTypeEnum.DNI, "123456789", "1234567890", true, null, null, RoleEnum.WAITER);

        when(persistencePort.findById(id)).thenReturn(Optional.of(user));
        when(persistencePort.save(user)).thenReturn(user);

        userService.toggleEnabled(id, false);

        assertFalse(user.getIsEnabled());
        verify(persistencePort).findById(id);
        verify(persistencePort).save(user);
    }


}
