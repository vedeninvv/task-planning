package com.practice.taskplanning.dto.user;

import com.practice.taskplanning.model.user.Role;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserPostDto {
    /**
     * Имя пользователя (логин)
     */
    @NotNull
    @Size(min = 5, max = 40)
    private String username;

    /**
     * Пароль пользователя
     */
    @NotNull
    @Size(min = 5, max = 40)
    private String password;

    /**
     * Множество ролей пользователя
     */
    @NotEmpty(message = "User must have at least one role")
    private Set<Role> roles;
}
