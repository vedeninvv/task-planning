package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.TaskPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Repository
public interface TaskPointRepository extends PagingAndSortingRepository<TaskPoint, Long> {
    Set<TaskPoint> findAllByIdIn(Collection<Long> ids);

    @Query("select tp from TaskPoint tp join tp.task t" +
            "   where (:taskId is null or t.id = :taskId)" +
            "   and (:completed is null or tp.completed = :completed)" +
            "   and (:searchStr is null or tp.name like %:searchStr% or tp.description like %:searchStr%)" +
            "   and (tp.createdDate between :createdDateBegin and :createdDateEnd)")
    Page<TaskPoint> findAllWithFilters(Long taskId, Boolean completed, String searchStr,
                                       Date createdDateBegin, Date createdDateEnd, Pageable pageable);
}
