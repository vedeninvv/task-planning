package com.practice.taskplanning.datasetup;

import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.model.user.RoleEntity;
import com.practice.taskplanning.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleSetUp implements DataSetUp {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleSetUp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Проверка существования ролей пользователей в БД и их генерация при необходимости после старта приложения
     */
    @EventListener(ApplicationReadyEvent.class)
    public void addingRolesToDbFromEnum() {
        List<Role> rolesInDatabase = ((Collection<RoleEntity>) roleRepository.findAll()).stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toList());
        if (!rolesInDatabase.containsAll(Arrays.stream(Role.values()).toList())) {
            roleRepository.deleteAll();
            for (Role role : Role.values()) {
                roleRepository.save(new RoleEntity(null, role));
            }
        }
    }
}
