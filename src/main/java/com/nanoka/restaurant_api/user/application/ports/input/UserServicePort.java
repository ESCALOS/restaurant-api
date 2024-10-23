package com.nanoka.restaurant_api.user.application.ports.input;

import com.nanoka.restaurant_api.user.domain.model.User;

import java.util.List;

public interface UserServicePort {
    User findById(Long id);
    List<User> findAll();
    User save(User user);
    User update(Long id, User user);
    void delete(Long id);
    void changePassword(String username,String currentPassword, String newPassword);
}
