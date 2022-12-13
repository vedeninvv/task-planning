package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.task.TaskExecutorsDto;
import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.model.task.TaskEntity;
import com.practice.taskplanning.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface TaskService {
    TaskGetDto createTask(TaskPostDto taskPostDto);

    TaskGetDto updateTask(Long taskId, TaskPatchDto taskPatchDto);

    TaskGetDto getTaskById(Long taskId);

    Page<TaskGetDto> getAllTasks(Status status, Long assignedUserId, Long assignedTeamId, String searchStr,
                                 Date createdDateBegin, Date createdDateEnd, Pageable pageable);

    TaskGetDto deleteTaskById(Long taskId);

    TaskGetDto assignUserToTask(Long taskId, Long userId);

    TaskGetDto assignToTask(Long taskId, TaskExecutorsDto taskExecutorsDto);

    TaskGetDto removeUserFromTask(Long taskId, Long userId);

    TaskGetDto removeFromTask(Long taskId, TaskExecutorsDto taskExecutorsDto);

    void updateTaskWhenTaskPointsChanged(TaskEntity task, Date currentDate);

    TaskEntity changeStatusIfNecessary(TaskEntity task, Date currentDate);

    Status determineCurrentStatus(TaskEntity task);

    boolean isUserAssignedToTask(UserEntity user, TaskEntity task);

    void updatingTaskStatusIfDeadlineOverdue();
}
