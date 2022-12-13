package com.practice.taskplanning.model;

import com.practice.taskplanning.model.task.TaskEntity;
import com.practice.taskplanning.model.user.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Команда - группа пользователей, которых можно назначить на задание
 */
@Entity
@Table(name = "team")
@Getter
@Setter
@RequiredArgsConstructor
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название команды
     */
    private String name;

    /**
     * Задания, на которые назначена команда
     */
    @ManyToMany(mappedBy = "assignedTeams")
    private Set<TaskEntity> tasksAssignedTo;

    /**
     * Участники команды
     */
    @ManyToMany
    @JoinTable(
            name = "team_appuser",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "appuser_id")
    )
    private Set<UserEntity> members = new HashSet<>();
}
