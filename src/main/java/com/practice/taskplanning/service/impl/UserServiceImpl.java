package com.practice.taskplanning.service.impl;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.exception.DuplicateUniqueValueException;
import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.mapper.UserMapper;
import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.model.user.UserEntity;
import com.practice.taskplanning.repository.UserRepository;
import com.practice.taskplanning.service.UserService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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
        UserEntity user = userMapper.toModel(userPostDto);
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

    public Page<UserGetDto> getAllUsers(Role role, Pageable pageable) {
        try {
            return userRepository.findAllByRole(role, pageable).map(userMapper::toDto);
        } catch (InvalidDataAccessApiUsageException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page parameters: " + exception.getMessage());
        }
    }

    public UserGetDto updateUser(Long userId, UserPatchDto userPatchDto) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
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

    public UserGetDto deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("User with id '%d' not found when try to delete user", userId));
        });
        user.getTeams().forEach(team -> team.getMembers().remove(user));
        user.getTasksAssignedTo().forEach(task -> task.getAssignedUsers().remove(user));
        userRepository.delete(user);
        return userMapper.toDto(user);
    }

    public UserGetDto getUserByUsername(String username) {
        return userMapper.toDto(
                userRepository.findByUsername(username).orElseThrow(() -> {
                    throw new NotFoundException(String.format("User with username '%s' not found when try to get user by username", username));
                })
        );
    }
}
