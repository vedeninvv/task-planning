package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.mapper.TaskPointMapper;
import com.practice.taskplanning.model.TaskPoint;
import com.practice.taskplanning.model.task.Task;
import com.practice.taskplanning.repository.TaskPointRepository;
import com.practice.taskplanning.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        taskService.updateTaskStatusWhenTaskPointsChanged(taskPoint, currentDate);
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

    public Iterable<TaskPointGetDto> getAllTaskPoints(Long taskId, Boolean completed, String searchStr,
                                                      Date createdDateBegin, Date createdDateEnd, Pageable pageable) {
        searchStr = searchStr != null ? searchStr.toLowerCase(Locale.ROOT) : searchStr;
        createdDateBegin = createdDateBegin != null ? createdDateBegin : new Date(0);
        createdDateEnd = createdDateEnd != null ? createdDateEnd : new GregorianCalendar(10000, Calendar.FEBRUARY, 1).getTime();
        return taskPointMapper.toDto(
                taskPointRepository.findAllWithFilters(taskId, completed, searchStr, createdDateBegin, createdDateEnd, pageable)
        );
    }

    public void deleteTaskPoint(Long taskPointId) {
        TaskPoint taskPoint = taskPointRepository.findById(taskPointId).orElseThrow(() -> {
            throw new NotFoundException(String.format("TaskPoint not found with id '%d' when try to delete taskPoint", taskPointId));
        });
        taskPointRepository.delete(taskPoint);
        taskService.updateTaskStatusWhenTaskPointsChanged(taskPoint, new Date());
    }
}
