package com.practice.taskplanning.dto.team;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class TeamPostDto {
    /**
     * Название команды
     */
    @Size(max = 40)
    @NotBlank
    private String name;

    /**
     * Множество ID пользователей-участников команды
     */
    private Set<Long> memberIds;
}
