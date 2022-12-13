package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.model.user.AppUser;
import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping(path = "/api/users",
        produces = "application/json")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    UserGetDto createUser(@RequestBody @Valid UserPostDto userPostDto) {
        return userService.createUser(userPostDto);
    }

    @PreAuthorize("hasAuthority('user:all')")
    @GetMapping("/{userId}")
    UserGetDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PreAuthorize("hasAuthority('user:all')")
    @GetMapping(params = {"username"})
    UserGetDto getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("hasAuthority('user:all')")
    @GetMapping
    Iterable<UserGetDto> getAllUsers(@RequestParam(required = false) Role role,
                                     @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return userService.getAllUsers(role, pageable);
    }

    @PreAuthorize("hasAuthority('user:all') and (hasRole('ADMIN') or #user.id == #userId)")
    @PatchMapping("/{userId}")
    UserGetDto updateUser(@PathVariable Long userId,
                          @RequestBody @Valid UserPatchDto userPatchDto,
                          @AuthenticationPrincipal AppUser user) {
        return userService.updateUser(userId, userPatchDto);
    }

    @PreAuthorize("hasAuthority('user:all') and (hasRole('ADMIN') or #user.id == #userId)")
    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId,
                    @AuthenticationPrincipal AppUser user) {
        userService.deleteUser(userId);
    }
}
