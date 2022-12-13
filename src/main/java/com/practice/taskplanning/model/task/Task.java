package com.practice.taskplanning.model.task;

import com.practice.taskplanning.model.TaskPoint;
import com.practice.taskplanning.model.Team;
import com.practice.taskplanning.model.user.AppUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Задание - то, что выполняют пользователи
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое наименование задачи
     */
    private String name;

    /**
     * Подробное описание задачи
     */
    private String description;

    /**
     * Дата создания задачи
     */
    private Date createdDate;

    /**
     * Текущий статус задачи
     */
    @Enumerated(value = EnumType.STRING)
    private Status status;

    /**
     * Дата обновления статуса задачи
     */
    private Date statusUpdated;

    /**
     * Срок выполнения задания
     */
    private Date deadline;

    /**
     * Подзадачи, из которых состоит задание
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private Set<TaskPoint> taskPoints = new HashSet<>();

    /**
     * Назначенные на задание пользователи
     */
    @ManyToMany
    @JoinTable(
            name = "task_appuser",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "appuser_id")
    )
    private Set<AppUser> assignedUsers = new HashSet<>();

    /**
     * Назначенные на задание команды
     */
    @ManyToMany
    @JoinTable(
            name = "task_team",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> assignedTeams = new HashSet<>();
}
