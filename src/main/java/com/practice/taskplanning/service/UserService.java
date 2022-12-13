package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.model.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserGetDto createUser(UserPostDto userPostDto);

    UserGetDto getUserById(Long userId);

    UserGetDto getUserByUsername(String username);

    Page<UserGetDto> getAllUsers(Role role, Pageable pageable);

    UserGetDto updateUser(Long userId, UserPatchDto userPatchDto);

    UserGetDto deleteUser(Long userId);
}
