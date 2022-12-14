package com.practice.taskplanning.model.user;

import com.practice.taskplanning.model.TeamEntity;
import com.practice.taskplanning.model.task.TaskEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "appuser")
@Getter
@Setter
@RequiredArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя
     */
    @Column(unique = true)
    private String username;

    /**
     * Пароль пользователя
     */
    private String password;

    /**
     * Роли пользователя
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "appuser_role",
            joinColumns = @JoinColumn(name = "appuser_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    /**
     * Задания, на которые назначен пользователь
     */
    @ManyToMany(mappedBy = "assignedUsers")
    private Set<TaskEntity> tasksAssignedTo;

    /**
     * Команды, в которых состоит пользователь
     */
    @ManyToMany(mappedBy = "members")
    private Set<TeamEntity> teams;

    /**
     * Получить разрешения пользователя в зависимости от его ролей
     *
     * @return коллекция разрешений
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .flatMap((roleEntity) -> roleEntity.getRole().getGrantedAuthorities().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

