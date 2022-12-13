package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.exception.DuplicateUniqueValueException;
import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.mapper.UserMapper;
import com.practice.taskplanning.model.user.AppUser;
import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.model.user.RoleEntity;
import com.practice.taskplanning.repository.RoleRepository;
import com.practice.taskplanning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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

    public UserGetDto createUser(UserPostDto userPostDto) {
        if (userRepository.existsByUsername(userPostDto.getUsername())) {
            throw new DuplicateUniqueValueException("Username already exists");
        }
        userPostDto.setPassword(
                passwordEncoder.encode(userPostDto.getPassword())
        );
        AppUser user = userMapper.toModel(userPostDto);
        return userMapper.toDto(
                userRepository.save(user)
        );
    }

    public UserGetDto getUserById(Long userId) {
        return userMapper.toDto(
                userRepository.findById(userId).orElseThrow(() -> {
                    throw new NotFoundException(String.format("User with id '%d' not found when try to get user by id", userId));
                })
        );
    }

    public Iterable<UserGetDto> getAllUsers(Role role, Pageable pageable) {
        try {
            return userMapper.toDto(
                    userRepository.findAllByRole(role, pageable)
            );
        } catch (InvalidDataAccessApiUsageException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page parameters: " + exception.getMessage());
        }
    }

    public UserGetDto updateUser(Long userId, UserPatchDto userPatchDto) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("User with id '%d' not found when try to update user", userId));
        });
        if (userPatchDto.getUsername() != null && !userPatchDto.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(userPatchDto.getUsername())) {
            throw new DuplicateUniqueValueException("Username already exists");
        }
        if (userPatchDto.getPassword() != null) {
            userPatchDto.setPassword(
                    passwordEncoder.encode(userPatchDto.getPassword())
            );
        }
        userMapper.updateModel(user, userPatchDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id '%d' not found when try to delete user", userId));
        }
        userRepository.deleteById(userId);
    }

    public UserGetDto getUserByUsername(String username) {
        return userMapper.toDto(
                userRepository.findByUsername(username).orElseThrow(() -> {
                    throw new NotFoundException(String.format("User with username '%s' not found when try to get user by username", username));
                })
        );
    }
}
