package com.practice.taskplanning.service;

import com.practice.taskplanning.dto.user.UserGetDto;
import com.practice.taskplanning.dto.user.UserPatchDto;
import com.practice.taskplanning.dto.user.UserPostDto;
import com.practice.taskplanning.model.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    /**
     * Создать пользователя
     *
     * @param userPostDto данные пользователя
     * @return данные созданного пользователя
     */
    UserGetDto createUser(UserPostDto userPostDto);

    /**
     * Получить пользователя по ID
     *
     * @param userId ID пользователя
     * @return данные пользователя
     */
    UserGetDto getUserById(Long userId);

    /**
     * Получить пользователя по имени пользователя (логину)
     *
     * @param username имя пользователя (логин)
     * @return данные пользователя
     */
    UserGetDto getUserByUsername(String username);

    /**
     * Находит всех пользователей, обладающих переданной ролью
     *
     * @param role     роль пользователя
     * @param pageable условия пагинации
     * @return Page пользователей
     */
    Page<UserGetDto> getAllUsers(Role role, Pageable pageable);

    /**
     * Обновить пользователя по ID
     *
     * @param userId       ID пользователя
     * @param userPatchDto данные обновления пользователя
     * @return данные обновленного пользователя
     */
    UserGetDto updateUser(Long userId, UserPatchDto userPatchDto);

    /**
     * Удалить пользователя по ID
     *
     * @param userId ID пользователя
     * @return данные удаленного пользователя
     */
    UserGetDto deleteUser(Long userId);
}
