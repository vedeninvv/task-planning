package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.model.user.UserEntity;
import com.practice.taskplanning.service.TaskPointService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(path = "/api/tasks",
        produces = "application/json")
public class TaskPointController {
    private final TaskPointService taskPointService;

    @Autowired
    public TaskPointController(TaskPointService taskPointService) {
        this.taskPointService = taskPointService;
    }

    @Operation(summary = "Create taskPoint", security = @SecurityRequirement(name = "basicAuth"),
            description = "Create taskPoint in connection with task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskPoint was created"),
            @ApiResponse(responseCode = "400", description = "Invalid taskPoint's data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to create taskPoint", content = @Content)
    })
    @PreAuthorize("hasAuthority('task_point:write')")
    @PostMapping("/{taskId}/task-points")
    public TaskPointGetDto createTaskPoint(@PathVariable Long taskId,
                                           @RequestBody @Valid TaskPointPostDto taskPointPostDto) {
        return taskPointService.createTaskPoint(taskId, taskPointPostDto);
    }

    @Operation(summary = "Update taskPoint", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskPoint was updated"),
            @ApiResponse(responseCode = "404", description = "TaskPoint not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid taskPoint's data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to update taskPoint", content = @Content)
    })
    @PreAuthorize("hasAuthority('task_point:write')")
    @PatchMapping("/task-points/{taskPointId}")
    public TaskPointGetDto updateTaskPoint(@PathVariable Long taskPointId,
                                           @RequestBody @Valid TaskPointPatchDto taskPointPatchDto) {
        return taskPointService.updateTaskPoint(taskPointId, taskPointPatchDto);
    }

    @Operation(summary = "Get taskPoint by id", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskPoint successfully found by id"),
            @ApiResponse(responseCode = "404", description = "TaskPoint not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to get taskPoint", content = @Content)
    })
    @PreAuthorize("hasAuthority('task_point:read')")
    @GetMapping("/task-points/{taskPointId}")
    public TaskPointGetDto getTaskPointById(@PathVariable Long taskPointId) {
        return taskPointService.getTaskPointById(taskPointId);
    }

    @Operation(summary = "Get page of taskPoints", security = @SecurityRequirement(name = "basicAuth"),
            description = "Get page of taskPoints with filters by task, completed status, matching search string with name" +
                    " or description, start date and end date (year-month-day)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of taskPoints"),
            @ApiResponse(responseCode = "400", description = "Bad parameters type or format", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to get taskPoints", content = @Content)
    })
    @PreAuthorize("hasAuthority('task_point:read')")
    @GetMapping("/task-points")
    public Page<TaskPointGetDto> getAllTaskPoints(@RequestParam(required = false) Long taskId,
                                                  @RequestParam(required = false) Boolean completed,
                                                  @RequestParam(required = false) String searchStr,
                                                  @Parameter(description = "Date format: 'yyyy-MM-dd'") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                  @RequestParam(required = false) Date createdDateBegin,
                                                  @Parameter(description = "Date format: 'yyyy-MM-dd'") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                  @RequestParam(required = false) Date createdDateEnd,
                                                  @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return taskPointService.getAllTaskPoints(taskId, completed, searchStr, createdDateBegin, createdDateEnd, pageable);
    }

    @Operation(summary = "Delete taskPoint", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskPoint was deleted"),
            @ApiResponse(responseCode = "404", description = "TaskPoint not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to delete taskPoint", content = @Content)
    })
    @PreAuthorize("hasAuthority('task_point:write')")
    @DeleteMapping("/task-points/{taskPointId}")
    public TaskPointGetDto deleteTaskPoint(@PathVariable Long taskPointId) {
        return taskPointService.deleteTaskPoint(taskPointId);
    }

    @Operation(summary = "Complete taskPoint", security = @SecurityRequirement(name = "basicAuth"),
            description = "Only admins and users, that assigned to taskPoint's task, can complete. " +
                    "Assigned users are all users that was assigned or participants of assigned teams")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskPoint was completed"),
            @ApiResponse(responseCode = "404", description = "TaskPoint not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not admin or assigned user", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('complete_task_point:assigned', 'complete_task_point:all')")
    @PostMapping("/task-points/{taskPointId}/complete")
    public TaskPointGetDto completeTaskPoint(@PathVariable Long taskPointId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal UserEntity user) {
        return taskPointService.completeTaskPoint(user, taskPointId);
    }

    @Operation(summary = "Rollback taskPoint", security = @SecurityRequirement(name = "basicAuth"),
            description = "Only admins and users, that assigned to taskPoint's task, can rollback. " +
                    "Assigned users are all users that was assigned or participants of assigned teams")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TaskPoint was rolled back"),
            @ApiResponse(responseCode = "404", description = "TaskPoint not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Not admin or assigned user", content = @Content)
    })
    @PreAuthorize("hasAnyAuthority('rollback_task_point:assigned', 'rollback_task_point:all')")
    @PostMapping("/task-points/{taskPointId}/rollback-complete")
    public TaskPointGetDto rollbackTaskPoint(@PathVariable Long taskPointId,
                                             @Parameter(hidden = true) @AuthenticationPrincipal UserEntity user) {
        return taskPointService.rollbackTaskPoint(user, taskPointId);
    }
}
