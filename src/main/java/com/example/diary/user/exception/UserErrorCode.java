package com.example.diary.user.exception;

import com.example.diary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    NOT_FOUND_SESSION(HttpStatus.UNAUTHORIZED, "세션이 존재하지 않습니다."),
    INVALID_SESSION(HttpStatus.UNAUTHORIZED, "세션이 유효하지 않습니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "이미 가입된 아이디입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "중복된 이름입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    INVALID_LOGIN_ID(HttpStatus.BAD_REQUEST, "일치하는 아이디가 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
