package com.practice.taskplanning.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
public class TaskPatchDto {
    @Size(max = 40)
    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    private Set<Long> taskPointsIds;

    private Set<Long> assignedUsersIds;

    private Set<Long> assignedTeamsIds;
}
