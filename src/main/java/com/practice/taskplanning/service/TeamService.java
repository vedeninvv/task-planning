package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.team.TeamGetDto;
import com.practice.taskplanning.dto.team.TeamPatchDto;
import com.practice.taskplanning.dto.team.TeamPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    TeamGetDto createTeam(TeamPostDto teamPostDto);

    TeamGetDto getTeamById(Long teamId);

    Page<TeamGetDto> getAllTeams(Long memberId, Pageable pageable);

    TeamGetDto updateTeam(Long teamId, TeamPatchDto teamPatchDto);

    TeamGetDto deleteTeamById(Long teamId);
}
