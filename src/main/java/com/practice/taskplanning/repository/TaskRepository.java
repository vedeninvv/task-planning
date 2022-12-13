package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.model.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    @Query("select distinct task from Task task left join task.assignedUsers user left join task.assignedTeams team" +
            "   where (:status is null or task.status = :status)" +
            "   and (:assignedUserId is null or user.id = :assignedUserId)" +
            "   and (:assignedTeamId is null  or team.id = :assignedTeamId)" +
            "   and (:searchStr is null or task.name like %:searchStr% or task.description like %:searchStr%)" +
            "   and (task.createdDate between :createdDateBegin and :createdDateEnd)")
    Page<Task> findAllWithFilters(Status status, Long assignedUserId, Long assignedTeamId,
                                  String searchStr, Date createdDateBegin, Date createdDateEnd, Pageable pageable);
}
