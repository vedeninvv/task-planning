package com.practice.taskplanning.dto.team;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class TeamPatchDto {
    @Size(max = 40)
    private String name;

    private Set<Long> memberIds;
}
