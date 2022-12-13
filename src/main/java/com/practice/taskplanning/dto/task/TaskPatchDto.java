package com.practice.taskplanning.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
public class TaskPatchDto {
    /**
     * Краткое имя задания
     */
    @Size(max = 40)
    private String name;

    /**
     * Подробное описание задания
     */
    private String description;

    /**
     * Крайний срок выполнения задания
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    /**
     * Множество ID подзадач задания
     */
    private Set<Long> taskPointsIds;

    /**
     * Множество ID пользователей, назначенных на задания
     */
    private Set<Long> assignedUsersIds;

    /**
     * Множество ID команд, назначенных на задания
     */
    private Set<Long> assignedTeamsIds;
}
