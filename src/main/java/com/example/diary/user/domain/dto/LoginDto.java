package com.example.diary.user.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDto {

    @NotNull
    private String loginId;

    @Size(min = 8, message = "비밀번호는 8자 이상")
    @Pattern(regexp = ".*[!@#$].*", message = "!, @, #, $가 포함되어야 합니다.")
    private String password;
}
