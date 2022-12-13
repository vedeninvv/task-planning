package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.model.user.AppUser;
import com.practice.taskplanning.model.user.Role;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserGetDto toDto(AppUser appUser);

    Iterable<UserGetDto> toDto(Iterable<AppUser> appUsers);

    AppUser toModel(UserPostDto userPostDto);

    void updateModel(@MappingTarget AppUser user, UserPatchDto userPatchDto);

    @Condition
    default boolean rolesIsNotEmpty(Set<Role> roles) {
        return roles != null && !roles.isEmpty();
    }
}
