package com.nanoka.restaurant_api.user.domain.model;

import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private RoleEnum roleEnum;

    private Set<Permission> permissions;

    public Role(Long id, RoleEnum roleEnum) {
        this.id = id;
        this.roleEnum = roleEnum;
    }
}
