package com.practice.taskplanning.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.model.task.Status;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class TaskGetDto {
    private Long id;

    /**
     * Краткое имя задания
     */
    private String name;

    /**
     * Подробное описание задания
     */
    private String description;

    /**
     * Дата создания задания
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    /**
     * Текущий статус задания
     */
    private Status status;

    /**
     * Дата последнего обновления статуса
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date statusUpdated;

    /**
     * Крайний срок выполнения задания
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    /**
     * Подзадачи задания
     */
    @JsonProperty("taskPoints")
    private Set<TaskPointGetDto> taskPointGetDtos = new HashSet<>();

    /**
     * Множество ID пользователей, назначенных на задания
     */
    private Set<Long> assignedUsersIds = new HashSet<>();

    /**
     * Множество ID команд, назначенных на задания
     */
    private Set<Long> assignedTeamsIds = new HashSet<>();
}
