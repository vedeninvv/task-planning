package com.practice.taskplanning.dto.team;

import com.practice.taskplanning.dto.user.UserGetDto;
import lombok.Data;

import java.util.List;

@Data
public class TeamGetDto {
    private Long id;

    /**
     * Название команды
     */
    private String name;

    /**
     * Список пользователей-участников команды
     */
    private List<UserGetDto> userGetDtos;
}
