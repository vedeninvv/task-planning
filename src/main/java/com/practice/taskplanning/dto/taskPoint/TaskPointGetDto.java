package com.practice.taskplanning.dto.taskPoint;

import lombok.Data;

import java.util.Date;

@Data
public class TaskPointGetDto {
    private Long id;

    private String name;

    private String description;

    private Date createdDate;

    private boolean completed;

    private Date completedDate;

    private Long taskId;
}
