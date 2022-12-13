package com.practice.taskplanning.dto.user;

import com.practice.taskplanning.model.user.Role;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserPatchDto {
    /**
     * Имя пользователя (логин)
     */
    @Size(min = 5, max = 40)
    private String username;

    /**
     * Пароль пользователя
     */
    @Size(min = 5, max = 40)
    private String password;

    /**
     * Множество ролей пользователя
     */
    private Set<Role> roles;
}
