package com.practice.taskplanning.service;

import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.model.user.RoleEntity;
import com.practice.taskplanning.repository.RoleRepository;
import com.practice.taskplanning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    private void init() {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(String.format("User with username '%s' not found", username));
        });
    }
}
