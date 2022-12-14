package com.practice.taskplanning.service.impl;

import com.practice.taskplanning.dto.task.TaskExecutorsDto;
import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.mapper.TaskMapper;
import com.practice.taskplanning.model.TaskPointEntity;
import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.model.task.TaskEntity;
import com.practice.taskplanning.model.user.UserEntity;
import com.practice.taskplanning.repository.TaskPointRepository;
import com.practice.taskplanning.repository.TaskRepository;
import com.practice.taskplanning.repository.TeamRepository;
import com.practice.taskplanning.repository.UserRepository;
import com.practice.taskplanning.service.TaskService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskPointRepository taskPointRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           TeamRepository teamRepository,
                           TaskPointRepository taskPointRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.taskPointRepository = taskPointRepository;
        this.taskMapper = taskMapper;
    }

    public TaskGetDto createTask(TaskPostDto taskPostDto) {
        final Date currentDate = new Date();

        TaskEntity task = taskMapper.toModel(taskPostDto);
        task.setCreatedDate(currentDate);

        task.getTaskPoints().forEach(taskPoint -> {
            taskPoint.setTask(task);
            taskPoint.setCreatedDate(currentDate);
        });

        if (taskPostDto.getAssignedUsersIds() != null) {
            task.setAssignedUsers(userRepository.findAllByIdIn(taskPostDto.getAssignedUsersIds()));
        }
        if (taskPostDto.getAssignedTeamsIds() != null) {
            task.setAssignedTeams(teamRepository.findAllByIdIn(taskPostDto.getAssignedTeamsIds()));
        }

        changeStatusIfNecessary(task, currentDate);

        return taskMapper.toDto(taskRepository.save(task));
    }

    public TaskGetDto getTaskById(Long taskId) {
        return taskMapper.toDto(
                taskRepository.findById(taskId).orElseThrow(() -> {
                    throw new NotFoundException(String.format("Task with id '%d' not found when try to get task", taskId));
                })
        );
    }

    public Page<TaskGetDto> getAllTasks(Status status, Long assignedUserId, Long assignedTeamId, String searchStr,
                                        Date createdDateBegin, Date createdDateEnd, Pageable pageable) {
        searchStr = searchStr != null ? searchStr.toLowerCase(Locale.ROOT) : searchStr;
        try {
            return taskRepository.findAllWithFilters(status, assignedUserId, assignedTeamId,
                            searchStr, createdDateBegin != null, createdDateBegin,
                            createdDateEnd != null, createdDateEnd, pageable)
                    .map(taskMapper::toDto);
        } catch (InvalidDataAccessApiUsageException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page parameters: " + exception.getMessage());
        }
    }

    public TaskGetDto updateTask(Long taskId, TaskPatchDto taskPatchDto) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to update", taskId));
        });

        taskMapper.updateModel(task, taskPatchDto);

        if (taskPatchDto.getAssignedUsersIds() != null) {
            task.setAssignedUsers(userRepository.findAllByIdIn(taskPatchDto.getAssignedUsersIds()));
        }
        if (taskPatchDto.getAssignedTeamsIds() != null) {
            task.setAssignedTeams(teamRepository.findAllByIdIn(taskPatchDto.getAssignedTeamsIds()));
        }
        if (taskPatchDto.getTaskPointsIds() != null) {
            task.getTaskPoints().clear();
            task.getTaskPoints().addAll(taskPointRepository.findAllByIdIn(taskPatchDto.getTaskPointsIds()));
        }

        changeStatusIfNecessary(task, new Date());

        return taskMapper.toDto(taskRepository.save(task));
    }

    public TaskGetDto deleteTaskById(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to delete task", taskId));
        });
        TaskGetDto taskGetDto = taskMapper.toDto(task);
        taskRepository.delete(task);
        return taskGetDto;
    }

    public TaskGetDto assignUserToTask(Long taskId, Long userId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to assign user with id '%d'", taskId, userId));
        });
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("User with id '%d' not found when try to assign to task with id '%d'", userId, taskId));
        });
        task.getAssignedUsers().add(user);
        changeStatusIfNecessary(task, new Date());
        return taskMapper.toDto(
                taskRepository.save(task)
        );
    }

    public TaskGetDto removeUserFromTask(Long taskId, Long userId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to remove user with id '%d' from task", taskId, userId));
        });
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("User with id '%d' not found when try to remove from task with id '%d'", userId, taskId));
        });
        if (task.getAssignedUsers().remove(user)) {
            changeStatusIfNecessary(task, new Date());
            return taskMapper.toDto(
                    taskRepository.save(task)
            );
        } else {
            throw new NotFoundException(String.format("User with id '%d' not found among assigned users of task with id '%d'", userId, taskId));
        }
    }

    public TaskGetDto assignToTask(Long taskId, TaskExecutorsDto taskExecutorsDto) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to assign", taskId));
        });
        if (taskExecutorsDto.getAssignedUsersIds() != null) {
            task.getAssignedUsers().addAll(userRepository.findAllByIdIn(taskExecutorsDto.getAssignedUsersIds()));
        }
        if (taskExecutorsDto.getAssignedTeamsIds() != null) {
            task.getAssignedTeams().addAll(teamRepository.findAllByIdIn(taskExecutorsDto.getAssignedTeamsIds()));
        }
        changeStatusIfNecessary(task, new Date());
        return taskMapper.toDto(
                taskRepository.save(task)
        );
    }

    public TaskGetDto removeFromTask(Long taskId, TaskExecutorsDto taskExecutorsDto) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to remove executors", taskId));
        });
        if (taskExecutorsDto.getAssignedUsersIds() != null) {
            task.getAssignedUsers().removeAll(userRepository.findAllByIdIn(taskExecutorsDto.getAssignedUsersIds()));
        }
        if (taskExecutorsDto.getAssignedTeamsIds() != null) {
            task.getAssignedTeams().removeAll(teamRepository.findAllByIdIn(taskExecutorsDto.getAssignedTeamsIds()));
        }
        changeStatusIfNecessary(task, new Date());
        return taskMapper.toDto(
                taskRepository.save(task)
        );
    }

    public void updateTaskWhenTaskPointsChanged(TaskEntity task, Date currentDate) {
        taskRepository.save(
                changeStatusIfNecessary(task, currentDate)
        );
    }

    public TaskEntity changeStatusIfNecessary(TaskEntity task, Date currentDate) {
        Status newStatus = determineCurrentStatus(task);
        if (task.getStatus() != newStatus) {
            task.setStatus(newStatus);
            task.setStatusUpdated(currentDate);
        }
        return task;
    }

    public Status determineCurrentStatus(TaskEntity task) {
        boolean isAssigned = !task.getAssignedUsers().isEmpty() || !task.getAssignedTeams().isEmpty();
        boolean isCompleted = task.getTaskPoints().stream().allMatch(TaskPointEntity::isCompleted);

        boolean isDateExpired;
        if (task.getStatus() == Status.COMPLETED) {
            isDateExpired = DateUtils.truncate(task.getStatusUpdated(), Calendar.DATE)
                    .after(DateUtils.truncate(task.getDeadline(), Calendar.DATE));
        } else {
            isDateExpired = DateUtils.truncate(task.getDeadline(), Calendar.DATE)
                    .before(DateUtils.truncate(new Date(), Calendar.DATE));
        }

        if (!isAssigned && !isCompleted && !isDateExpired) {
            return Status.CREATED;
        } else if (isAssigned && !isCompleted && !isDateExpired) {
            return Status.ASSIGNED;
        } else if (isCompleted && !isDateExpired) {
            return Status.COMPLETED;
        } else if (!isCompleted) {
            return Status.DATE_EXPIRED;
        } else {
            return Status.LATE_COMPLETED;
        }
    }

    public boolean isUserAssignedToTask(UserEntity user, TaskEntity task) {
        boolean isAssignedUser = task.getAssignedUsers().contains(user);
        boolean inAssignedTeam = task.getAssignedTeams().stream().anyMatch(team -> team.getMembers().contains(user));
        return isAssignedUser || inAssignedTeam;
    }

    @Transactional
    @Scheduled(cron = "@daily")
    public void updatingTaskStatusIfDeadlineOverdue() {
        taskRepository.updateStatusIfCurrentInListAndDeadlineBeforeDate(
                Status.DATE_EXPIRED,
                List.of(Status.CREATED, Status.ASSIGNED),
                new Date());
    }
}
