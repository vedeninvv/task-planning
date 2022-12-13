package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.TaskPoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface TaskPointRepository extends PagingAndSortingRepository<TaskPoint, Long> {
    Set<TaskPoint> findAllByIdIn(Collection<Long> ids);
}
