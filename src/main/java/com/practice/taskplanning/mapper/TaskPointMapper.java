package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.model.TaskPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskPointMapper {
    TaskPoint toModel(TaskPointPostDto taskPointPostDto);

    void updateModel(@MappingTarget TaskPoint taskPoint, TaskPointPatchDto taskPointPatchDto);

    @Mapping(target = "taskId", source = "taskPoint.task.id")
    TaskPointGetDto toDto(TaskPoint taskPoint);

    Iterable<TaskPointGetDto> toDto(Iterable<TaskPoint> taskPoints);
}
