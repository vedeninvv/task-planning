package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.model.TeamEntity;
import com.practice.taskplanning.model.task.TaskEntity;
import com.practice.taskplanning.model.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TaskPointMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    @Mapping(target = "taskPoints", source = "taskPointPostDtos")
    TaskEntity toModel(TaskPostDto taskPostDto);

    void updateModel(@MappingTarget TaskEntity task, TaskPatchDto taskPatchDto);

    @Mapping(target = "taskPointGetDtos", source = "taskPoints")
    @Mapping(target = "assignedUsersIds", source = "assignedUsers")
    @Mapping(target = "assignedTeamsIds", source = "assignedTeams")
    TaskGetDto toDto(TaskEntity task);

    Iterable<TaskGetDto> toDto(Iterable<TaskEntity> tasks);

    default Set<Long> getAssignedUsersId(Set<UserEntity> assignedUsers) {
        return assignedUsers.stream().map(UserEntity::getId).collect(Collectors.toSet());
    }

    default Set<Long> getAssignedTeamsId(Set<TeamEntity> assignedTeams) {
        return assignedTeams.stream().map(TeamEntity::getId).collect(Collectors.toSet());
    }
}
