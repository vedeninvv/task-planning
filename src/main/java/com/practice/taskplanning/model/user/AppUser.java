package com.practice.taskplanning.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "appuser")
@Getter
@Setter
@RequiredArgsConstructor
public class AppUser implements UserDetails {
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
     * Получить разрешения пользователя в зависимости от его ролей
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
}

