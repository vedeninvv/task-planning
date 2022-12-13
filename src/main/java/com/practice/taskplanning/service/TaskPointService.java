package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.model.TaskPointEntity;
import com.practice.taskplanning.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface TaskPointService {
    TaskPointGetDto createTaskPoint(Long taskId, TaskPointPostDto taskPointPostDto);

    TaskPointGetDto updateTaskPoint(Long taskPointId, TaskPointPatchDto taskPointPatchDto);

    TaskPointGetDto getTaskPointById(Long taskPointId);

    Page<TaskPointGetDto> getAllTaskPoints(Long taskId, Boolean completed, String searchStr,
                                           Date createdDateBegin, Date createdDateEnd, Pageable pageable);

    TaskPointGetDto deleteTaskPoint(Long taskPointId);

    TaskPointGetDto completeTaskPoint(UserEntity user, Long taskPointId);

    TaskPointGetDto rollbackTaskPoint(UserEntity user, Long taskPointId);

    boolean hasAccessToComplete(UserEntity user, TaskPointEntity taskPoint);

    boolean hasAccessToRollback(UserEntity user, TaskPointEntity taskPoint);
}
