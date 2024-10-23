package com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserPersistenceMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(mapPassword(user))")
    public abstract UserEntity toUserEntity(User user);

    public abstract User toUser(UserEntity entity);

    public abstract List<User> toUserList(List<UserEntity> entityList);

    protected String mapPassword(User user) {
        return passwordEncoder.encode(user.getPassword());
    }
}
