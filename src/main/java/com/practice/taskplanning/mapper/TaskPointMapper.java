package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.model.TaskPointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskPointMapper {
    TaskPointEntity toModel(TaskPointPostDto taskPointPostDto);

    void updateModel(@MappingTarget TaskPointEntity taskPoint, TaskPointPatchDto taskPointPatchDto);

    @Mapping(target = "taskId", source = "taskPoint.task.id")
    TaskPointGetDto toDto(TaskPointEntity taskPoint);

    Iterable<TaskPointGetDto> toDto(Iterable<TaskPointEntity> taskPoints);
}
