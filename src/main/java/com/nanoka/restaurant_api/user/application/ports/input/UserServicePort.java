package com.nanoka.restaurant_api.user.application.ports.input;

import com.nanoka.restaurant_api.user.domain.model.User;

import java.util.List;

public interface UserServicePort {
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    User save(User user);
    User update(Long id, User user);
    void delete(Long id);
    void changePassword(String username,String currentPassword, String newPassword);
    void passwordReset(Long id, String newPassword);
    void toggleEnabled(Long id, Boolean isEnabled);
}
