package com.practice.taskplanning.dto.taskPoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TaskPointGetDto {
    private Long id;

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    private boolean completed;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date completedDate;

    private Long taskId;
}
