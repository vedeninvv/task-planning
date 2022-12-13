package com.practice.taskplanning.dto.task;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class TaskPointPostDto {
    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    private String description;
}
