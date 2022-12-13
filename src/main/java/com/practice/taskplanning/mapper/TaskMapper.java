package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.model.Team;
import com.practice.taskplanning.model.task.Task;
import com.practice.taskplanning.model.user.AppUser;
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
    Task toModel(TaskPostDto taskPostDto);

    void updateModel(@MappingTarget Task task, TaskPatchDto taskPatchDto);

    @Mapping(target = "taskPointGetDtos", source = "taskPoints")
    @Mapping(target = "assignedUsersIds", source = "assignedUsers")
    @Mapping(target = "assignedTeamsIds", source = "assignedTeams")
    TaskGetDto toDto(Task task);

    default Set<Long> getAssignedUsersId(Set<AppUser> assignedUsers) {
        return assignedUsers.stream().map(AppUser::getId).collect(Collectors.toSet());
    }

    default Set<Long> getAssignedTeamsId(Set<Team> assignedTeams) {
        return assignedTeams.stream().map(Team::getId).collect(Collectors.toSet());
    }
}
