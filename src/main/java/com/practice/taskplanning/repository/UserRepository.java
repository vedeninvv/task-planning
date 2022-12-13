package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.user.AppUser;
import com.practice.taskplanning.model.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends PagingAndSortingRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("select distinct user from AppUser user join user.roles role where role.role = ?1 or ?1 is null")
    Page<AppUser> findAllByRole(Role role, Pageable pageable);

    Set<AppUser> findAllByIdIn(Collection<Long> ids);
}
