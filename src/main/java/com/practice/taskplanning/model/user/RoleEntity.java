package com.practice.taskplanning.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Сущность роли пользователя
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип роли
     */
    @Enumerated(EnumType.STRING)
    @Column()
    private Role role;
}
