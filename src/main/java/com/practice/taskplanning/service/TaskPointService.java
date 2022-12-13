package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.exception.UserNotAssigned;
import com.practice.taskplanning.mapper.TaskPointMapper;
import com.practice.taskplanning.model.TaskPoint;
import com.practice.taskplanning.model.task.Task;
import com.practice.taskplanning.model.user.AppUser;
import com.practice.taskplanning.model.user.Permission;
import com.practice.taskplanning.repository.TaskPointRepository;
import com.practice.taskplanning.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service
public class TaskPointService {
    private final TaskPointRepository taskPointRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final TaskPointMapper taskPointMapper;

    @Autowired
    public TaskPointService(TaskPointRepository taskPointRepository, TaskRepository taskRepository, TaskService taskService, TaskPointMapper taskPointMapper) {
        this.taskPointRepository = taskPointRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.taskPointMapper = taskPointMapper;
    }

    public TaskPointGetDto createTaskPoint(Long taskId, TaskPointPostDto taskPointPostDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(
                    String.format("Task not found with id '%d' when try to create taskPoint related to this task", taskId));
        });
        TaskPoint taskPoint = taskPointMapper.toModel(taskPointPostDto);
        taskPoint.setTask(task);
        Date currentDate = new Date();
        taskPoint.setCreatedDate(currentDate);
        taskPoint = taskPointRepository.save(taskPoint);
        taskService.updateTaskWhenTaskPointsChanged(taskPoint.getTask(), currentDate);
        return taskPointMapper.toDto(taskPoint);
    }

    public TaskPointGetDto updateTaskPoint(Long taskPointId, TaskPointPatchDto taskPointPatchDto) {
        TaskPoint taskPoint = taskPointRepository.findById(taskPointId).orElseThrow(() -> {
            throw new NotFoundException(String.format("TaskPoint not found with id '%d' when try to update taskPoint", taskPointId));
        });
        taskPointMapper.updateModel(taskPoint, taskPointPatchDto);
        return taskPointMapper.toDto(
                taskPointRepository.save(taskPoint)
        );
    }

    public TaskPointGetDto getTaskPointById(Long taskPointId) {
        return taskPointMapper.toDto(
                taskPointRepository.findById(taskPointId).orElseThrow(() -> {
                    throw new NotFoundException(String.format("TaskPoint not found with id '%d' when try to get taskPoint", taskPointId));
                })
        );
    }

    public Page<TaskPointGetDto> getAllTaskPoints(Long taskId, Boolean completed, String searchStr,
                                                  Date createdDateBegin, Date createdDateEnd, Pageable pageable) {
        searchStr = searchStr != null ? searchStr.toLowerCase(Locale.ROOT) : searchStr;
        return taskPointRepository.findAllWithFilters(taskId, completed, searchStr, createdDateBegin != null,
                        createdDateBegin, createdDateEnd != null, createdDateEnd, pageable)
                .map(taskPointMapper::toDto);
    }

    public void deleteTaskPoint(Long taskPointId) {
        TaskPoint taskPoint = taskPointRepository.findById(taskPointId).orElseThrow(() -> {
            throw new NotFoundException(String.format("TaskPoint not found with id '%d' when try to delete taskPoint", taskPointId));
        });
        taskPointRepository.delete(taskPoint);
        taskService.updateTaskWhenTaskPointsChanged(taskPoint.getTask(), new Date());
    }

    public TaskPointGetDto completeTaskPoint(AppUser user, Long taskPointId) {
        TaskPoint taskPoint = taskPointRepository.findById(taskPointId).orElseThrow(() -> {
            throw new NotFoundException(String.format("TaskPoint not found with id '%d' when try to complete taskPoint", taskPointId));
        });
        if (hasAccessToComplete(user, taskPoint)) {
            Date currentDate = new Date();
            taskPoint.setCompletedDate(currentDate);
            taskPoint.setCompleted(true);
            taskService.updateTaskWhenTaskPointsChanged(taskPoint.getTask(), currentDate);
            return taskPointMapper.toDto(taskPoint);
        } else {
            throw new UserNotAssigned(
                    String.format("User with id '%d' not assigned to task '%d' to complete taskPoint with id '%d'",
                            user.getId(), taskPoint.getTask().getId(), taskPointId)
            );
        }
    }

    public boolean hasAccessToComplete(AppUser user, TaskPoint taskPoint) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority(Permission.COMPLETE_ALL_TASK_POINT.getPermission()))) {
            return true;
        } else {
            return taskService.isUserAssignedToTask(user, taskPoint.getTask());
        }
    }

    public TaskPointGetDto rollbackTaskPoint(AppUser user, Long taskPointId) {
        TaskPoint taskPoint = taskPointRepository.findById(taskPointId).orElseThrow(() -> {
            throw new NotFoundException(String.format("TaskPoint not found with id '%d' when try to rollback taskPoint", taskPointId));
        });
        if (hasAccessToRollback(user, taskPoint)) {
            taskPoint.setCompletedDate(null);
            taskPoint.setCompleted(false);
            taskService.updateTaskWhenTaskPointsChanged(taskPoint.getTask(), new Date());
            return taskPointMapper.toDto(taskPoint);
        } else {
            throw new UserNotAssigned(
                    String.format("User with id '%d' not assigned to task '%d' to rollback taskPoint with id '%d'",
                            user.getId(), taskPoint.getTask().getId(), taskPointId)
            );
        }
    }

    public boolean hasAccessToRollback(AppUser user, TaskPoint taskPoint) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority(Permission.ROLLBACK_ALL_TASK_POINT.getPermission()))) {
            return true;
        } else {
            return taskService.isUserAssignedToTask(user, taskPoint.getTask());
        }
    }
}
