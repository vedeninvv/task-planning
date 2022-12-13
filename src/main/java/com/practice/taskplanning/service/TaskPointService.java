package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.taskPoint.TaskPointGetDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPatchDto;
import com.practice.taskplanning.dto.taskPoint.TaskPointPostDto;
import com.practice.taskplanning.model.TaskPointEntity;
import com.practice.taskplanning.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface TaskPointService {
    /**
     * Создать подзадачу для задания
     *
     * @param taskId           ID задания, к которому относится подзадача
     * @param taskPointPostDto данные подзадачи
     * @return данные созданной подзадачи
     */
    TaskPointGetDto createTaskPoint(Long taskId, TaskPointPostDto taskPointPostDto);

    /**
     * Обновить подзадачу по ID
     *
     * @param taskPointId       ID подзадачи
     * @param taskPointPatchDto данные обновления подзадачи
     * @return данные обновленной подзадачи
     */
    TaskPointGetDto updateTaskPoint(Long taskPointId, TaskPointPatchDto taskPointPatchDto);

    /**
     * Получить подзадачу по ID
     *
     * @param taskPointId ID подзадачи
     * @return данные подзадачи
     */
    TaskPointGetDto getTaskPointById(Long taskPointId);

    /**
     * Находит все подзадачи, удовлетворяющие заданным параметрам
     *
     * @param taskId           ID задания подзадач
     * @param completed        выполнена ли подзадача
     * @param searchStr        строка, входящая в имя или описание
     * @param createdDateBegin дата создания, не раньше которой требуются подзадачи
     * @param createdDateEnd   дата создания, не позже которой требуются подзадачи
     * @param pageable         условия пагинации
     * @return Page подзадач
     */
    Page<TaskPointGetDto> getAllTaskPoints(Long taskId, Boolean completed, String searchStr,
                                           Date createdDateBegin, Date createdDateEnd, Pageable pageable);

    /**
     * Удалить подзадачу по ID
     *
     * @param taskPointId ID подзадачи
     * @return данные удаленной подзадачи
     */
    TaskPointGetDto deleteTaskPoint(Long taskPointId);

    /**
     * Выполнить подзадачу
     *
     * @param user        пользователь, выполняющий подзадачу
     * @param taskPointId ID подзадачи
     * @return данные выполненной подзадачи
     */
    TaskPointGetDto completeTaskPoint(UserEntity user, Long taskPointId);

    /**
     * Вернуть подзадачу к невыполненному состоянию
     *
     * @param user        пользователь, возвращающий к невыполненному состоянию
     * @param taskPointId ID подзадачи
     * @return данные подзадачи, вернувшейся к невыполненному состоянию
     */
    TaskPointGetDto rollbackTaskPoint(UserEntity user, Long taskPointId);

    /**
     * Проверяет, может ли пользователь выполнить подзадачу
     *
     * @param user      проверяемый пользователь
     * @param taskPoint выполняемая подзадача
     * @return true, если пользователь может выполнить подзадачу
     */
    boolean hasAccessToComplete(UserEntity user, TaskPointEntity taskPoint);

    /**
     * Проверяет, может ли пользователь вернуть подзадачу к невыполненному состоянию
     *
     * @param user      проверяемый пользователь
     * @param taskPoint подзадача, возвращаемая к невыполненному состоянию
     * @return true, если пользователь может вернуть подзадачу к невыполненному состоянию
     */
    boolean hasAccessToRollback(UserEntity user, TaskPointEntity taskPoint);
}
