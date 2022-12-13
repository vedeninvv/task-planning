package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.task.TaskExecutorsDto;
import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.model.user.AppUser;
import com.practice.taskplanning.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(path = "/api/tasks",
        produces = "application/json")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create task", security = @SecurityRequirement(name = "basicAuth"),
            description = "Create task together with taskPoints: must contain at least one")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task was created"),
            @ApiResponse(responseCode = "400", description = "Invalid task's data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to create task", content = @Content)
    })
    @PreAuthorize("hasAuthority('task:write')")
    @PostMapping
    public TaskGetDto createTask(@Valid @RequestBody TaskPostDto taskPostDto) {
        return taskService.createTask(taskPostDto);
    }

    @Operation(summary = "Update task", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task was updated"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid task's data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to update task", content = @Content)
    })
    @PreAuthorize("hasAuthority('task:write')")
    @PatchMapping("/{taskId}")
    public TaskGetDto updateTask(@PathVariable Long taskId,
                                 @Valid @RequestBody TaskPatchDto taskPatchDto) {
        return taskService.updateTask(taskId, taskPatchDto);
    }

    @Operation(summary = "Get task by id", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task successfully found by id"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to get task", content = @Content)
    })
    @PreAuthorize("hasAuthority('task:read')")
    @GetMapping("/{taskId}")
    public TaskGetDto getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @Operation(summary = "Get page of tasks", security = @SecurityRequirement(name = "basicAuth"),
            description = "Get page of tasks with filters by status, assigned user or team, matching search string with a name" +
                    " or description, start date and end date (year-month-day)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of tasks"),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to get tasks", content = @Content)
    })
    @PreAuthorize("hasAuthority('task:read')")
    @GetMapping()
    public Iterable<TaskGetDto> getAllTasks(@RequestParam(required = false) Status status,
                                            @RequestParam(required = false) Long assignedUserId,
                                            @RequestParam(required = false) Long assignedTeamId,
                                            @Parameter(description = "Search by part of name or description")
                                            @RequestParam(required = false) String searchStr,
                                            @Parameter(description = "Date format: 'yyyy-MM-dd'") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                            @RequestParam(required = false) Date createdDateBegin,
                                            @Parameter(description = "Date format: 'yyyy-MM-dd'") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                            @RequestParam(required = false) Date createdDateEnd,
                                            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return taskService.getAllTasks(status, assignedUserId, assignedTeamId, searchStr,
                createdDateBegin, createdDateEnd, pageable);
    }

    @Operation(summary = "Delete task", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task was deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to delete task", content = @Content)
    })
    @PreAuthorize("hasAuthority('task:write')")
    @DeleteMapping("/{taskId}")
    public void deleteTaskById(@PathVariable Long taskId) {
        taskService.deleteTaskById(taskId);
    }

    @Operation(summary = "Assign user to task", security = @SecurityRequirement(name = "basicAuth"),
    description = "Admins can assign to task all users. Users can assign only themselves")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was assigned to task"),
            @ApiResponse(responseCode = "404", description = "Task or user not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not admin or attempt to assign to task not himself", content = @Content)
    })
    @PreAuthorize("hasAuthority('assign_to_task:all') or (hasAuthority('assign_to_task:self') and #user.id == #userId)")
    @PostMapping("/{taskId}/assigned-users/{userId}")
    public TaskGetDto assignUserToTask(@PathVariable Long taskId,
                                       @PathVariable Long userId,
                                       @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return taskService.assignUserToTask(taskId, userId);
    }

    @Operation(summary = "Assign executors to task", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Executors was assigned to task"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to assign all executors", content = @Content)
    })
    @PreAuthorize("hasAuthority('assign_to_task:all')")
    @PostMapping("/{taskId}/assigne-executors")
    public TaskGetDto assignToTask(@PathVariable Long taskId, @RequestBody TaskExecutorsDto taskExecutorsDto) {
        return taskService.assignToTask(taskId, taskExecutorsDto);
    }

    @Operation(summary = "Remove user from task", security = @SecurityRequirement(name = "basicAuth"),
            description = "Admins can remove from task all users. Users can remove only themselves")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was removed from task"),
            @ApiResponse(responseCode = "404", description = "Task or user not found. Or user not found among assigned users", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not admin or attempt to remove from task not himself", content = @Content)
    })
    @PreAuthorize("hasAuthority('remove_from_task:all') or (hasAuthority('remove_from_task:self') and #user.id == #userId)")
    @DeleteMapping("/{taskId}/assigned-users/{userId}")
    public TaskGetDto removeUserFromTask(@PathVariable Long taskId,
                                         @PathVariable Long userId,
                                         @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return taskService.removeUserFromTask(taskId, userId);
    }

    @Operation(summary = "Remove executors from task", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Executors was removed from task"),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to remove all executors", content = @Content)
    })
    @PreAuthorize("hasAuthority('remove_from_task:all')")
    @DeleteMapping("/{taskId}/remove-executors")
    public TaskGetDto removeFromTask(@PathVariable Long taskId, @RequestBody TaskExecutorsDto taskExecutorsDto) {
        return taskService.removeFromTask(taskId, taskExecutorsDto);
    }
}
