package com.example.diary.group.exception;

import com.example.diary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorCode {
    DUPLICATE_GROUP_NAME(HttpStatus.CONFLICT, "이미 존재하는 그룹 이름입니다."),
    INVALID_GROUP_ID(HttpStatus.BAD_REQUEST, "요청하신 그룹이 없습니다.."),
    UNAUTHORIZED_ROLE(HttpStatus.BAD_REQUEST, "그룹을 삭제할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
