package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.task.TaskExecutorsDto;
import com.practice.taskplanning.dto.task.TaskGetDto;
import com.practice.taskplanning.dto.task.TaskPatchDto;
import com.practice.taskplanning.dto.task.TaskPostDto;
import com.practice.taskplanning.model.task.Status;
import com.practice.taskplanning.model.task.TaskEntity;
import com.practice.taskplanning.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface TaskService {
    /**
     * Создать задание
     *
     * @param taskPostDto данные задания
     * @return данные созданного задания
     */
    TaskGetDto createTask(TaskPostDto taskPostDto);

    /**
     * Обновить задание по ID
     *
     * @param taskId       ID задания
     * @param taskPatchDto данные обновления задания
     * @return данные обновленного задания
     */
    TaskGetDto updateTask(Long taskId, TaskPatchDto taskPatchDto);

    /**
     * Получить задание по ID
     *
     * @param taskId ID задания
     * @return данные задания
     */
    TaskGetDto getTaskById(Long taskId);

    /**
     * Находит все задания, удовлетворяющие заданным параметрам
     *
     * @param status           статус задания
     * @param assignedUserId   ID назначенного на задание пользователя
     * @param assignedTeamId   ID назначенной на задание команды
     * @param searchStr        строка, входящая в имя или описание
     * @param createdDateBegin дата создания, не раньше которой требуются задачи
     * @param createdDateEnd   дата создания, не позже которой требуются задачи
     * @param pageable         условия пагинации
     * @return Page заданий
     */
    Page<TaskGetDto> getAllTasks(Status status, Long assignedUserId, Long assignedTeamId, String searchStr,
                                 Date createdDateBegin, Date createdDateEnd, Pageable pageable);

    /**
     * Удалить задание по ID
     *
     * @param taskId ID задания
     * @return данные удаленного задания
     */
    TaskGetDto deleteTaskById(Long taskId);

    /**
     * Назначить пользователя на задание
     *
     * @param taskId ID задания, на которое назначают
     * @param userId ID назначаемого пользователя
     * @return данные задания, на которое назначен пользователь
     */
    TaskGetDto assignUserToTask(Long taskId, Long userId);

    /**
     * Назначить на задание исполнителей: пользователей и команды
     *
     * @param taskId           ID задания, на которое назначают
     * @param taskExecutorsDto данные исполнителей
     * @return данные задания, на которое назначены исполнители
     */
    TaskGetDto assignToTask(Long taskId, TaskExecutorsDto taskExecutorsDto);

    /**
     * Удалить пользователя из назначенных на задание пользователей
     *
     * @param taskId ID задания, с которого удаляют пользователя
     * @param userId ID удаляемого пользователя
     * @return данные задания, с которого удален пользователь
     */
    TaskGetDto removeUserFromTask(Long taskId, Long userId);

    /**
     * Удалить с задачи назначенных исполнителей пользователей и команды
     *
     * @param taskId           ID задания, с которого удаляют исполнителей
     * @param taskExecutorsDto данные исполнителей
     * @return данные задания, с которого удалены исполнители
     */
    TaskGetDto removeFromTask(Long taskId, TaskExecutorsDto taskExecutorsDto);

    /**
     * Обновляет задание (статус и дату последнего обновления статуса), когда изменяются подзадачи задания:
     * их состав или состояние выполненности одной из подзадач
     *
     * @param task        задание, подзадачи которого изменились
     * @param currentDate текущая дата (будет проставлена заданию в качестве даты последнего обновления статуса)
     */
    void updateTaskWhenTaskPointsChanged(TaskEntity task, Date currentDate);

    /**
     * Изменяет статус задания, если внутреннее состояние задание соответствует другому статусу
     *
     * @param task        задание, статус которого требуется изменить
     * @param currentDate текущая дата (будет проставлена заданию в качестве даты последнего обновления статуса)
     * @return задание с измененным статусом
     */
    TaskEntity changeStatusIfNecessary(TaskEntity task, Date currentDate);

    /**
     * Определяет текущий статус задания по внутренним характеристикам
     *
     * @param task задание, статус которого требуется определить
     * @return текущий статус задания
     */
    Status determineCurrentStatus(TaskEntity task);

    /**
     * Определяет назначен ли пользователь на задание. Пользователь считается назначенным, если он назначен прямым
     * образом (находится в множестве назначенных на задание пользователей) или является участником назначенной
     * на задание команды
     *
     * @param user проверяемый пользователь
     * @param task задание, для которого осуществляется проверка
     * @return true, если пользователь назначен
     */
    boolean isUserAssignedToTask(UserEntity user, TaskEntity task);

    /**
     * Обновляет статусы всех заданий, чей крайний строк выполнения истек
     */
    void updatingTaskStatusIfDeadlineOverdue();
}
