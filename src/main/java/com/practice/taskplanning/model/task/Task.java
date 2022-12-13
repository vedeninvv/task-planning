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

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Date createdDate;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private Date statusUpdated;

    private Date deadline;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    private Set<TaskPoint> taskPoints = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "task_appuser",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "appuser_id")
    )
    private Set<AppUser> assignedUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "task_team",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> assignedTeams = new HashSet<>();
}
