package com.practice.taskplanning.dto.user;

import com.practice.taskplanning.model.user.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserGetDto {
    private Long id;
    private String username;
    private Set<Role> roles;
}
