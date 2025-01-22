package com.example.diary.group.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateGroupDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
