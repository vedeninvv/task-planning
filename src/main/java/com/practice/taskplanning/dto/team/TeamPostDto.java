package com.practice.taskplanning.dto.team;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class TeamPostDto {
    @Size(max = 40)
    @NotBlank
    private String name;

    private Set<Long> memberIds;
}
