package com.practice.taskplanning.service.impl;

import com.practice.taskplanning.dto.team.TeamGetDto;
import com.practice.taskplanning.dto.team.TeamPatchDto;
import com.practice.taskplanning.dto.team.TeamPostDto;
import com.practice.taskplanning.exception.NotFoundException;
import com.practice.taskplanning.mapper.TeamMapper;
import com.practice.taskplanning.model.TeamEntity;
import com.practice.taskplanning.model.user.UserEntity;
import com.practice.taskplanning.repository.TeamRepository;
import com.practice.taskplanning.repository.UserRepository;
import com.practice.taskplanning.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamMapper = teamMapper;
    }

    public TeamGetDto createTeam(TeamPostDto teamPostDto) {
        TeamEntity team = teamMapper.toModel(teamPostDto);
        if (teamPostDto.getMemberIds() != null) {
            team.setMembers(userRepository.findAllByIdIn(teamPostDto.getMemberIds()));
        }
        return teamMapper.toDto(teamRepository.save(team));
    }

    public TeamGetDto getTeamById(Long teamId) {
        return teamMapper.toDto(teamRepository.findById(teamId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Team with id '%d' not found when try to get by id", teamId));
        }));
    }

    public Page<TeamGetDto> getAllTeams(Long memberId, Pageable pageable) {
        if (memberId == null) {
            try {
                return teamRepository.findAll(pageable).map(teamMapper::toDto);
            } catch (PropertyReferenceException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page parameters: " + exception.getMessage());
            }
        } else {
            UserEntity member = userRepository.findById(memberId).orElseThrow(() -> {
                throw new NotFoundException(String.format("User with id '%d' not found when try to filter teams", memberId));
            });
            try {
                return teamRepository.getAllByMembersContaining(member, pageable).map(teamMapper::toDto);
            } catch (PropertyReferenceException exception) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page parameters: " + exception.getMessage());
            }
        }
    }

    public TeamGetDto updateTeam(Long teamId, TeamPatchDto teamPatchDto) {
        TeamEntity team = teamRepository.findById(teamId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Team with id '%d' not found when try to update team", teamId));
        });
        teamMapper.updateModel(team, teamPatchDto);
        if (teamPatchDto.getMemberIds() != null) {
            team.setMembers(userRepository.findAllByIdIn(teamPatchDto.getMemberIds()));
        }
        return teamMapper.toDto(teamRepository.save(team));
    }

    public TeamGetDto deleteTeamById(Long teamId) {
        TeamEntity team = teamRepository.findById(teamId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Team with id '%d' not found when try to delete", teamId));
        });
        teamRepository.delete(team);
        return teamMapper.toDto(team);
    }
}
