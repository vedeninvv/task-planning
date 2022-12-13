package com.practice.taskplanning.dto.taskPoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TaskPointGetDto {
    private Long id;

    /**
     * Краткое имя подзадачи
     */
    private String name;

    /**
     * Подробное описание подзадачи
     */
    private String description;

    /**
     * Дата создания подзадачи
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    /**
     * Выполнена ли подзадача
     */
    private boolean completed;

    /**
     * Дата выполнения подзадачи
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date completedDate;

    /**
     * ID задачи, к которой относится подзадача
     */
    private Long taskId;
}
