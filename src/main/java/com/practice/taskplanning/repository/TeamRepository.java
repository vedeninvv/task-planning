package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.Team;
import com.practice.taskplanning.model.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {
    Page<Team> getAllByMembersContaining(AppUser member, Pageable pageable);

    Set<Team> findAllByIdIn(Collection<Long> ids);
}
