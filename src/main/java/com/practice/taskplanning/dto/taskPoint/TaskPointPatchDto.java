package com.practice.taskplanning.dto.taskPoint;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TaskPointPatchDto {
    @Size(max = 40)
    private String name;

    private String description;
}
