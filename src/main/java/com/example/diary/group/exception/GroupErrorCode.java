package com.example.diary.group.exception;

import com.example.diary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorCode {
    DUPLICATE_GROUP_NAME(HttpStatus.CONFLICT, "이미 존재하는 그룹 이름입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
