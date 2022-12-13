package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.model.user.UserEntity;
import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Operation(summary = "Create user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was created"),
            @ApiResponse(responseCode = "400", description = "Invalid data or username already exists", content = @Content)
    })
    @PostMapping
    UserGetDto createUser(@RequestBody @Valid UserPostDto userPostDto) {
        return userService.createUser(userPostDto);
    }

    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully found by id"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to users", content = @Content)
    })
    @PreAuthorize("hasAuthority('user:read:all')")
    @GetMapping("/id/{userId}")
    UserGetDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Get user by username", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully found by username"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to users", content = @Content)
    })
    @PreAuthorize("hasAuthority('user:read:all')")
    @GetMapping("/{username}")
    UserGetDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @Operation(summary = "Get page of users", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of users"),
            @ApiResponse(responseCode = "400", description = "Invalid role", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to users", content = @Content)
    })
    @PreAuthorize("hasAuthority('user:read:all')")
    @GetMapping
    Page<UserGetDto> getAllUsers(@RequestParam(required = false) Role role,
                                 @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return userService.getAllUsers(role, pageable);
    }

    @Operation(summary = "Update user", security = @SecurityRequirement(name = "basicAuth"),
            description = "Update user. Can only be performed by admin or user himself")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was updated"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data or username already exists", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not admin or not this user", content = @Content)
    })
    @PreAuthorize("hasAuthority('user:write:all') or (hasAuthority('user:write:self') and #user.id == #userId)")
    @PatchMapping("/{userId}")
    UserGetDto updateUser(@PathVariable Long userId,
                          @RequestBody @Valid UserPatchDto userPatchDto,
                          @Parameter(hidden = true) @AuthenticationPrincipal UserEntity user) {
        return userService.updateUser(userId, userPatchDto);
    }

    @Operation(summary = "Delete user", security = @SecurityRequirement(name = "basicAuth"),
            description = "Delete user. Can only be performed by admin or user himself")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was deleted"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not admin or not this user", content = @Content)
    })
    @PreAuthorize("hasAuthority('user:write:all') or (hasAuthority('user:write:self') and #user.id == #userId)")
    @DeleteMapping("/{userId}")
    UserGetDto deleteUser(@PathVariable Long userId,
                    @Parameter(hidden = true) @AuthenticationPrincipal UserEntity user) {
        return userService.deleteUser(userId);
    }
}
