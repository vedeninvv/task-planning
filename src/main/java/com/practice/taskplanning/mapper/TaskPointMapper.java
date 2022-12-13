package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.task.TaskPointGetDto;
import com.practice.taskplanning.dto.task.TaskPointPostDto;
import com.practice.taskplanning.model.TaskPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskPointMapper {
    TaskPoint toModel(TaskPointPostDto taskPointPostDto);

    @Mapping(target = "taskId", source = "taskPoint.task.id")
    TaskPointGetDto toDto(TaskPoint taskPoint);
}
