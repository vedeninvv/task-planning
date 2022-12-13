package com.practice.taskplanning.dto.taskPoint;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class TaskPointPostDto {
    /**
     * Краткое имя подзадачи
     */
    @NotBlank
    @Size(max = 40)
    private String name;

    /**
     * Подробное описание подзадачи
     */
    @NotBlank
    private String description;
}
