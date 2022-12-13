package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.team.TeamGetDto;
import com.practice.taskplanning.dto.team.TeamPatchDto;
import com.practice.taskplanning.dto.team.TeamPostDto;
import com.practice.taskplanning.service.TeamService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/teams",
        produces = "application/json")
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PreAuthorize("hasAuthority('team:write')")
    @PostMapping
    public TeamGetDto createTeam(@Valid @RequestBody TeamPostDto teamPostDto) {
        return teamService.createTeam(teamPostDto);
    }

    @PreAuthorize("hasAuthority('team:read')")
    @GetMapping("/{teamId}")
    public TeamGetDto getTeamById(@PathVariable Long teamId) {
        return teamService.getTeamById(teamId);
    }

    @PreAuthorize("hasAuthority('team:read')")
    @GetMapping
    public Iterable<TeamGetDto> getAllTeams(@RequestParam(required = false) Long memberId,
                                            @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return teamService.getAllTeams(memberId, pageable);
    }

    @PreAuthorize("hasAuthority('team:write')")
    @PatchMapping("/{teamId}")
    public TeamGetDto updateTeam(@PathVariable Long teamId,
                                 @Valid @RequestBody TeamPatchDto teamPatchDto) {
        return teamService.updateTeam(teamId, teamPatchDto);
    }

    @PreAuthorize("hasAuthority('team:write')")
    @DeleteMapping("/{teamId}")
    public void deleteById(@PathVariable Long teamId) {
        teamService.deleteTeamById(teamId);
    }
}
