package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.service.TaskService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('task:write')")
    @PostMapping
    public TaskGetDto createTask(@Valid @RequestBody TaskPostDto taskPostDto) {
        return taskService.createTask(taskPostDto);
    }

    @PreAuthorize("hasAuthority('task:write')")
    @PatchMapping("/{taskId}")
    public TaskGetDto updateTask(@PathVariable Long taskId,
                                 @Valid @RequestBody TaskPatchDto taskPatchDto) {
        return taskService.updateTask(taskId, taskPatchDto);
    }
}