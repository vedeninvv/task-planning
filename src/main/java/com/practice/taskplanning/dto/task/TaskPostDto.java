package com.practice.taskplanning.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
public class TaskPostDto {
    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    private String description;

    @Future
    private Date deadline;

    @JsonProperty("taskPoints")
    @NotEmpty
    private Set<TaskPointPostDto> taskPointPostDtos;

    private Set<Long> assignedUsersIds;

    private Set<Long> assignedTeamsIds;
}