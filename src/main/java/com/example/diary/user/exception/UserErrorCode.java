package com.example.diary.user.exception;

import com.example.diary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    NOT_FOUND_SESSION(HttpStatus.UNAUTHORIZED, "세션이 존재하지 않습니다."),
    INVALID_SESSION(HttpStatus.UNAUTHORIZED, "세션이 유효하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
