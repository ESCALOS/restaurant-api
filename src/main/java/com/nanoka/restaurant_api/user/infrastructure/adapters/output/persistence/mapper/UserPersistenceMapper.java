package com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.user.infrastructure.adapters.output.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserPersistenceMapper {

    public abstract UserEntity toUserEntity(User user);

    public abstract User toUser(UserEntity entity);

    public abstract List<User> toUserList(List<UserEntity> entityList);

}
