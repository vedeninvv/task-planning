package com.practice.taskplanning.mapper;

import com.practice.taskplanning.dto.team.TeamGetDto;
import com.practice.taskplanning.dto.team.TeamPatchDto;
import com.practice.taskplanning.dto.team.TeamPostDto;
import com.practice.taskplanning.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TeamMapper {
    Team toModel(TeamPostDto teamPostDto);

    @Mapping(source = "members", target = "userGetDtos")
    TeamGetDto toDto(Team team);

    Iterable<TeamGetDto> toDto(Iterable<Team> teams);

    void updateModel(@MappingTarget Team team, TeamPatchDto teamPatchDto);
}
