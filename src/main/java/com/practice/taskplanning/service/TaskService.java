package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.task.TaskExecutorsDto;
import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.mapper.TaskMapper;
import com.practice.taskplanning.model.TaskPoint;
import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.model.task.Task;
import com.practice.taskplanning.model.user.AppUser;
import com.practice.taskplanning.repository.TaskPointRepository;
import com.practice.taskplanning.repository.TaskRepository;
import com.practice.taskplanning.repository.TeamRepository;
import com.practice.taskplanning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Locale;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskPointRepository taskPointRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository,
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

        Task task = taskMapper.toModel(taskPostDto);
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
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
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

    public void deleteTaskById(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to delete task", taskId));
        }
        taskRepository.deleteById(taskId);
    }

    public TaskGetDto assignUserToTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to assign user with id '%d'", taskId, userId));
        });
        AppUser user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("User with id '%d' not found when try to assign to task with id '%d'", userId, taskId));
        });
        task.getAssignedUsers().add(user);
        changeStatusIfNecessary(task, new Date());
        return taskMapper.toDto(
                taskRepository.save(task)
        );
    }

    public TaskGetDto removeUserFromTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Task with id '%d' not found when try to remove user with id '%d' from task", taskId, userId));
        });
        AppUser user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("User with id '%d' not found when try to remove from task with id '%d'", userId, taskId));
        });
        if (task.getAssignedUsers().contains(user)) {
            task.getAssignedUsers().remove(user);
            changeStatusIfNecessary(task, new Date());
            return taskMapper.toDto(
                    taskRepository.save(task)
            );
        } else {
            throw new NotFoundException(String.format("User with id '%d' not found among assigned users of task with id '%d'", userId, taskId));
        }
    }

    public TaskGetDto assignToTask(Long taskId, TaskExecutorsDto taskExecutorsDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
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
        Task task = taskRepository.findById(taskId).orElseThrow(() -> {
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

    public void updateTaskWhenTaskPointsChanged(Task task, Date currentDate) {
        taskRepository.save(
                changeStatusIfNecessary(task, currentDate)
        );
    }

    public Task changeStatusIfNecessary(Task task, Date currentDate) {
        Status newStatus = determineCurrentStatus(task);
        if (task.getStatus() != newStatus) {
            task.setStatus(newStatus);
            task.setStatusUpdated(currentDate);
        }
        return task;
    }

    public Status determineCurrentStatus(Task task) {
        boolean isAssigned = !task.getAssignedUsers().isEmpty() || !task.getAssignedTeams().isEmpty();
        boolean isCompleted = task.getTaskPoints().stream().allMatch(TaskPoint::isCompleted);

        boolean isDateExpired;
        if (task.getStatus() == Status.COMPLETED) {
            isDateExpired = task.getStatusUpdated().after(task.getDeadline());
        } else {
            isDateExpired = task.getDeadline().before(new Date());
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

    public boolean isUserAssignedToTask(AppUser user, Task task) {
        boolean isAssignedUser = task.getAssignedUsers().contains(user);
        boolean inAssignedTeam = task.getAssignedTeams().stream().anyMatch(team -> team.getMembers().contains(user));
        return isAssignedUser || inAssignedTeam;
    }
}
