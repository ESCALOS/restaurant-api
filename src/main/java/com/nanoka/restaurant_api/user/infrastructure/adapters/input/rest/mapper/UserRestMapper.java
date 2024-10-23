package com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.user.domain.model.Role;
import com.nanoka.restaurant_api.user.domain.model.RoleEnum;
import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.request.UserCreateRequest;
import com.nanoka.restaurant_api.user.infrastructure.adapters.input.rest.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRestMapper {
    User toUser(UserCreateRequest user);
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> userList);

    default Set<Role> mapRoles(List<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> {
                    RoleEnum roleEnum = RoleEnum.valueOf(roleName); // Convertir String a RoleEnum
                    Long roleId = getRoleIdByEnum(roleEnum); // Asignar el ID correcto
                    return new Role(roleId, roleEnum); // Crear el Role basado en el RoleEnum y el ID
                })
                .collect(Collectors.toSet());
    }

    default Long getRoleIdByEnum(RoleEnum roleEnum) {
        return switch (roleEnum) {
            case ADMIN -> 1L;
            case WAITER -> 2L;
            case STOREKEEPER -> 3L;
            default -> throw new IllegalArgumentException("Invalid role enum");
        };
    }
}
