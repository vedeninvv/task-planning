package com.practice.taskplanning.model;

import com.practice.taskplanning.model.task.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Подзадача, из которых состоят задания
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class TaskPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое наименование подзадачи
     */
    private String name;

    /**
     * Подробное описание подзадачи
     */
    private String description;

    /**
     * Дата создания подзадачи
     */
    private Date createdDate;

    /**
     * Выполнена ли подзадача
     */
    private boolean completed = false;

    /**
     * Дата выполнения подзадачи
     */
    private Date completedDate;

    /**
     * Задание, которому принадлежит подзадача
     */
    @ManyToOne(optional = false)
    private Task task;
}
