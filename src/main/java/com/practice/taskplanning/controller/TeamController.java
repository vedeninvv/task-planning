package com.practice.taskplanning.controller;

import com.practice.taskplanning.dto.team.TeamGetDto;
import com.practice.taskplanning.dto.team.TeamPatchDto;
import com.practice.taskplanning.dto.team.TeamPostDto;
import com.practice.taskplanning.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Operation(summary = "Create team", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team was created"),
            @ApiResponse(responseCode = "400", description = "Invalid team's data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to create team", content = @Content)
    })
    @PreAuthorize("hasAuthority('team:write')")
    @PostMapping
    public TeamGetDto createTeam(@Valid @RequestBody TeamPostDto teamPostDto) {
        return teamService.createTeam(teamPostDto);
    }

    @Operation(summary = "Get team by id", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team successfully found by id"),
            @ApiResponse(responseCode = "404", description = "Team not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to get teams", content = @Content)
    })
    @PreAuthorize("hasAuthority('team:read')")
    @GetMapping("/{teamId}")
    public TeamGetDto getTeamById(@PathVariable Long teamId) {
        return teamService.getTeamById(teamId);
    }

    @Operation(summary = "Get page of teams", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of teams"),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to get teams", content = @Content)
    })
    @PreAuthorize("hasAuthority('team:read')")
    @GetMapping
    public Page<TeamGetDto> getAllTeams(@RequestParam(required = false) Long memberId,
                                        @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return teamService.getAllTeams(memberId, pageable);
    }

    @Operation(summary = "Update team", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team was updated"),
            @ApiResponse(responseCode = "404", description = "Team not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid team's data", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to update team", content = @Content)
    })
    @PreAuthorize("hasAuthority('team:write')")
    @PatchMapping("/{teamId}")
    public TeamGetDto updateTeam(@PathVariable Long teamId,
                                 @Valid @RequestBody TeamPatchDto teamPatchDto) {
        return teamService.updateTeam(teamId, teamPatchDto);
    }

    @Operation(summary = "Delete team", security = @SecurityRequirement(name = "basicAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team was deleted"),
            @ApiResponse(responseCode = "404", description = "Team not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Id isn't number", content = @Content),
            @ApiResponse(responseCode = "403", description = "Don't have permission to delete team", content = @Content)
    })

    @PreAuthorize("hasAuthority('team:write')")
    @DeleteMapping("/{teamId}")
    public void deleteById(@PathVariable Long teamId) {
        teamService.deleteTeamById(teamId);
    }
}
