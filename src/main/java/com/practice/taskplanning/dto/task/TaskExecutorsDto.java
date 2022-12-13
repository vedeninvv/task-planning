package com.practice.taskplanning.dto.task;

import lombok.Data;

import java.util.Set;

@Data
public class TaskExecutorsDto {
    /**
     * Множество ID пользователей, которых необходимо назначить на задание
     */
    private Set<Long> assignedUsersIds;

    /**
     * Множество ID команд, которых необходимо назначить на задание
     */
    private Set<Long> assignedTeamsIds;
}
