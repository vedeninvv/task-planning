package com.practice.taskplanning.model;

import com.practice.taskplanning.model.task.TaskEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Подзадача, из которых состоят задания
 */
@Entity
@Table(name = "task_point")
@Getter
@Setter
@RequiredArgsConstructor
public class TaskPointEntity {
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
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    /**
     * Выполнена ли подзадача
     */
    private boolean completed = false;

    /**
     * Дата выполнения подзадачи
     */
    @Temporal(TemporalType.DATE)
    private Date completedDate;

    /**
     * Задание, которому принадлежит подзадача
     */
    @ManyToOne(optional = false)
    private TaskEntity task;
}
