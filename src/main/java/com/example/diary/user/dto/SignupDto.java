package com.example.diary.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SignupDto {

    @NotNull
    private String loginId;

    @Size(min = 8, message = "비밀번호는 8자 이상")
    @Pattern(regexp = ".*[!@#$].*", message = "!, @, #, $가 포함되어야 합니다.")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "영어, 한글, 숫자만 가능합니다.")
    private String nickname;

    @Email
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
}
