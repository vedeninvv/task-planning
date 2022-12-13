package com.practice.taskplanning.dto.taskPoint;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TaskPointPatchDto {
    /**
     * Краткое имя подзадачи
     */
    @Size(max = 40)
    private String name;

    /**
     * Подробное описание подзадачи
     */
    private String description;
}
