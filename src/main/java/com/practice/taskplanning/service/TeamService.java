package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.team.TeamGetDto;
import com.practice.taskplanning.dto.team.TeamPatchDto;
import com.practice.taskplanning.dto.team.TeamPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    /**
     * Создать команду
     *
     * @param teamPostDto данные команды
     * @return данные созданной команды
     */
    TeamGetDto createTeam(TeamPostDto teamPostDto);

    /**
     * Получить команду по ID
     *
     * @param teamId ID команды
     * @return данные команды
     */
    TeamGetDto getTeamById(Long teamId);

    /**
     * Находит все команды по участнику
     *
     * @param memberId ID участника
     * @param pageable условия пагинации
     * @return Page команд
     */
    Page<TeamGetDto> getAllTeams(Long memberId, Pageable pageable);

    /**
     * Обновить команду по ID
     *
     * @param teamId       ID команды
     * @param teamPatchDto данные обновления команды
     * @return данные обновленной команды
     */
    TeamGetDto updateTeam(Long teamId, TeamPatchDto teamPatchDto);

    /**
     * Удалить команду по ID
     *
     * @param teamId ID команды
     * @return данные удаленной команды
     */
    TeamGetDto deleteTeamById(Long teamId);
}
