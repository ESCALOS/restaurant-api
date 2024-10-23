package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request.UserCreateRequest;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRestMapper {
    User toUser(UserCreateRequest user);
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> userList);
}
