package com.practice.taskplanning.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
public class TaskPostDto {
    /**
     * Краткое имя задания
     */
    @NotBlank
    @Size(max = 40)
    private String name;

    /**
     * Подробное описание задания
     */
    @NotBlank
    private String description;

    /**
     * Крайний срок выполнения задания
     */
    @Future
    private Date deadline;

    /**
     * Подзадачи задания
     */
    @JsonProperty("taskPoints")
    @NotEmpty
    private Set<TaskPointPostDto> taskPointPostDtos;

    /**
     * Множество ID пользователей, назначенных на задания
     */
    private Set<Long> assignedUsersIds;

    /**
     * Множество ID команд, назначенных на задания
     */
    private Set<Long> assignedTeamsIds;
}
