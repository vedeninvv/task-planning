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

    /**
     * Находит все подзадачи, удовлетворяющие заданным параметрам
     * @param taskId id задание подзадач
     * @param completed выполнена ли подзадача
     * @param searchStr строка, входящая в имя или описание
     * @param hasCreatedDateBegin наличие даты создания, не раньше которой требуются подзадачи
     * @param createdDateBegin дата создания, не раньше которой требуются подзадачи
     * @param hasCreatedDateEnd наличие даты создания, не позже которой требуются подзадачи
     * @param createdDateEnd дата создания, не позже которой требуются подзадачи
     * @param pageable условия пагинации
     * @return Page подзадач
     */
    @Query("select tp from TaskPoint tp join tp.task t" +
            "   where (:taskId is null or t.id = :taskId)" +
            "   and (:completed is null or tp.completed = :completed)" +
            "   and (:searchStr is null or tp.name like %:searchStr% or tp.description like %:searchStr%)" +
            "   and (:hasCreatedDateBegin = false or tp.createdDate >= :createdDateBegin)" +
            "   and (:hasCreatedDateEnd = false or tp.createdDate <= :createdDateEnd)")
    Page<TaskPoint> findAllWithFilters(Long taskId, Boolean completed, String searchStr, boolean hasCreatedDateBegin,
                                       Date createdDateBegin, boolean hasCreatedDateEnd, Date createdDateEnd, Pageable pageable);
}
