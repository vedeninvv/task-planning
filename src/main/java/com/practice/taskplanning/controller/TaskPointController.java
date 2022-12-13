package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.service.TaskPointService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('task_point:write')")
    @PostMapping("/{taskId}/task-points")
    public TaskPointGetDto createTaskPoint(@PathVariable Long taskId,
                                           @RequestBody @Valid TaskPointPostDto taskPointPostDto) {
        return taskPointService.createTaskPoint(taskId, taskPointPostDto);
    }

    @PreAuthorize("hasAuthority('task_point:write')")
    @PatchMapping("/task-points/{taskPointId}")
    public TaskPointGetDto updateTaskPoint(@PathVariable Long taskPointId,
                                           @RequestBody @Valid TaskPointPatchDto taskPointPatchDto) {
        return taskPointService.updateTaskPoint(taskPointId, taskPointPatchDto);
    }

    @PreAuthorize("hasAuthority('task_point:read')")
    @GetMapping("/task-points/{taskPointId}")
    public TaskPointGetDto getTaskPointById(@PathVariable Long taskPointId) {
        return taskPointService.getTaskPointById(taskPointId);
    }

    @PreAuthorize("hasAuthority('task_point:read')")
    @GetMapping("/task-points")
    public Iterable<TaskPointGetDto> getAllTaskPoints(@RequestParam(required = false) Long taskId,
                                                      @RequestParam(required = false) Boolean completed,
                                                      @RequestParam(required = false) String searchStr,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date createdDateBegin,
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) Date createdDateEnd,
                                                      @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return taskPointService.getAllTaskPoints(taskId, completed, searchStr, createdDateBegin, createdDateEnd, pageable);
    }

    @PreAuthorize("hasAuthority('task_point:write')")
    @DeleteMapping("/task-points/{taskPointId}")
    public void deleteTaskPoint(@PathVariable Long taskPointId) {
        taskPointService.deleteTaskPoint(taskPointId);
    }
}
