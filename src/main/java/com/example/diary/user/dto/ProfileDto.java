package com.example.diary.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class ProfileDto {
    private String loginId;

    private String nickname;

    private String email;

    private LocalDate birth;
}
