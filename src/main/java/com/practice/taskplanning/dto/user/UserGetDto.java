package com.practice.taskplanning.dto.user;

import com.practice.taskplanning.model.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserGetDto {
    private Long id;

    /**
     * Имя пользователя (логин)
     */
    private String username;

    /**
     * Множество ролей пользователя
     */
    private Set<Role> roles;
}
