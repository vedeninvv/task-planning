package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.TeamEntity;
import com.practice.taskplanning.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface TeamRepository extends PagingAndSortingRepository<TeamEntity, Long> {
    Page<TeamEntity> getAllByMembersContaining(UserEntity member, Pageable pageable);

    Set<TeamEntity> findAllByIdIn(Collection<Long> ids);
}
