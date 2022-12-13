package com.practice.taskplanning.dto.team;

import com.practice.taskplanning.dto.user.UserGetDto;
import lombok.Data;

import java.util.List;

@Data
public class TeamGetDto {
    private Long id;
    private String name;
    private List<UserGetDto> userGetDtos;
}
