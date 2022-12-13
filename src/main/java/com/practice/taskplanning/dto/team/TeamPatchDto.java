package com.practice.taskplanning.dto.team;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class TeamPatchDto {
    @Size(max = 40)
    private String name;

    private List<Long> memberIds;
}
