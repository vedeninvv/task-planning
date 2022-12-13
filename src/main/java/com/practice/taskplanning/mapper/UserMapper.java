package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.model.user.UserEntity;
import com.practice.taskplanning.model.user.Role;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserGetDto toDto(UserEntity userEntity);

    Iterable<UserGetDto> toDto(Iterable<UserEntity> appUsers);

    UserEntity toModel(UserPostDto userPostDto);

    void updateModel(@MappingTarget UserEntity user, UserPatchDto userPatchDto);

    @Condition
    default boolean rolesIsNotEmpty(Set<Role> roles) {
        return roles != null && !roles.isEmpty();
    }
}
