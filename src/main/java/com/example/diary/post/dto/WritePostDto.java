package com.example.diary.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WritePostDto {

    @NotBlank
    private String title;

    @NotBlank
    private String body;
}
