package com.practice.taskplanning.dto.user;

import com.practice.taskplanning.model.user.Role;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserPatchDto {
    @Size(min = 5, max = 40)
    private String username;

    @Size(min = 5, max = 40)
    private String password;

    private Set<Role> roles;
}
