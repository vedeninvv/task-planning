package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.model.user.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(Role role);
}
