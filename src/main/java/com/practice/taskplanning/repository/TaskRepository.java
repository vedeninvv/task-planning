package com.practice.taskplanning.repository;

import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.model.task.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<TaskEntity, Long> {
    /**
     * Находит все задания, удовлетворяющие заданным параметрам
     *
     * @param status              статус задания
     * @param assignedUserId      ID назначенного на задание пользователя
     * @param assignedTeamId      ID назначенной на задание команды
     * @param searchStr           строка, входящая в имя или описание
     * @param hasCreatedDateBegin наличие даты создания, не раньше которой требуются задачи
     * @param createdDateBegin    дата создания, не раньше которой требуются задачи
     * @param hasCreatedDateEnd   наличие даты создания, не позже которой требуются задачи
     * @param createdDateEnd      дата создания, не позже которой требуются задачи
     * @param pageable            условия пагинации
     * @return Page заданий
     */
    @Query("select distinct task from TaskEntity task left join task.assignedUsers user left join task.assignedTeams team" +
            "   where (:status is null or task.status = :status)" +
            "   and (:assignedUserId is null or user.id = :assignedUserId)" +
            "   and (:assignedTeamId is null  or team.id = :assignedTeamId)" +
            "   and (:searchStr is null or task.name like %:searchStr% or task.description like %:searchStr%)" +
            "   and (:hasCreatedDateBegin = false or task.createdDate >= :createdDateBegin)" +
            "   and (:hasCreatedDateEnd = false or task.createdDate <= :createdDateEnd)")
    Page<TaskEntity> findAllWithFilters(Status status, Long assignedUserId, Long assignedTeamId,
                                        String searchStr, boolean hasCreatedDateBegin, Date createdDateBegin,
                                        boolean hasCreatedDateEnd, Date createdDateEnd, Pageable pageable);

    /**
     * Обновляет статусы всех заданий на переданный, текущий статус которых - один из переданного списка статусов, и
     * крайний срок выполнения - до переданной даты
     *
     * @param newStatus          новый статус заданий
     * @param statusListToUpdate статусы, с которыми нужно обновить задания
     * @param date               дата, до которой должен находиться крайний срок для обновления
     */
    @Modifying
    @Query("update TaskEntity task set task.status = :newStatus, task.statusUpdated = :date" +
            "   where (task.status in :statusListToUpdate)" +
            "   and (task.deadline < :date )")
    void updateStatusIfCurrentInListAndDeadlineBeforeDate(Status newStatus, List<Status> statusListToUpdate, Date date);
}
