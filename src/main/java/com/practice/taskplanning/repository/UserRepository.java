package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.user.Role;
import com.practice.taskplanning.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    /**
     * Находит всех пользователей, имеющих переданную роль
     *
     * @param role     роль
     * @param pageable условия пагинации
     * @return Page пользователей
     */
    @Query("select distinct user from UserEntity user join user.roles role where role.role = ?1 or ?1 is null")
    Page<UserEntity> findAllByRole(Role role, Pageable pageable);

    Set<UserEntity> findAllByIdIn(Collection<Long> ids);
}
