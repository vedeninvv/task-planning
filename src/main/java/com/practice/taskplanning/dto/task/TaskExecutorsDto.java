package com.practice.taskplanning.dto.task;

import lombok.Data;

import java.util.Set;

@Data
public class TaskExecutorsDto {
    private Set<Long> assignedUsersIds;

    private Set<Long> assignedTeamsIds;
}
