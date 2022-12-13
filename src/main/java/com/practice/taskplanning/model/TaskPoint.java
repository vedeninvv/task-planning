package com.practice.taskplanning.model;

import com.practice.taskplanning.model.task.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class TaskPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Date createdDate;

    private boolean completed = false;

    private Date completedDate;

    @ManyToOne(optional = false)
    private Task task;
}
