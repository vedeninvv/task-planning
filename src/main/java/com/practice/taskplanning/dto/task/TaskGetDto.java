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

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date statusUpdated;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    @JsonProperty("taskPoints")
    private Set<TaskPointGetDto> taskPointGetDtos = new HashSet<>();

    private Set<Long> assignedUsersIds = new HashSet<>();

    private Set<Long> assignedTeamsIds = new HashSet<>();
}
