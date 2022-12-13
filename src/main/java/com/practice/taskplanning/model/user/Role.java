package com.practice.taskplanning.model.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.practice.taskplanning.model.user.Permission.*;

public enum Role {
    ADMIN(Set.of(USER_ALL, TEAM_WRITE, TEAM_READ, TASK_WRITE, TASK_READ, TASK_POINT_WRITE, TASK_POINT_READ,
            ASSIGN_ALL_TO_TASK, REMOVE_ALL_FROM_TASK, COMPLETE_ALL_TASK_POINT, ROLLBACK_ALL_TASK_POINT)),
    USER(Set.of(USER_ALL, TEAM_READ, TASK_READ, TASK_POINT_READ, ASSIGN_SELF_TO_TASK, REMOVE_SELF_FROM_TASK,
            COMPLETE_ASSIGNED_TASK_POINT, ROLLBACK_ASSIGNED_TASK_POINT));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
