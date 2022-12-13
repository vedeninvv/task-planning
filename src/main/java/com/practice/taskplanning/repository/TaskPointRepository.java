package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.TaskPoint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPointRepository extends PagingAndSortingRepository<TaskPoint, Long> {
}
