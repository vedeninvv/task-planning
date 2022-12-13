package com.practice.taskplanning.mapper;

import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.model.user.RoleEntity;
import com.practice.taskplanning.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {
    @Autowired
    private RoleRepository roleRepository;

    public Role toEnum(RoleEntity roleEntity) {
        return roleEntity.getRole();
    }

    public RoleEntity toModel(Role role) {
        return roleRepository.findByRole(role).orElseThrow(() -> {
            throw new NotFoundException(String.format("Role '%s' not found when try to map to model", role.name()));
        });
    }

    public abstract Set<Role> toEnum(Set<RoleEntity> roleEntities);

    public abstract Set<RoleEntity> toModel(Set<Role> roles);
}
