package com.example.diary.user_group.exception;

import com.example.diary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserGroupErrorCode implements ErrorCode {

    INVALID_USER_AND_GROUP_ID(HttpStatus.BAD_REQUEST, "요청하신 사용자와 그룹이 일치하는 데이터가 없습니다."),
    DUPLICATE_USER_GROUP(HttpStatus.CONFLICT, "이미 가입된(또는 요청된) 그룹입니다."),
    UNAUTHORIZED_ROLE(HttpStatus.BAD_REQUEST, "관리자(OWNER, MANAGER)는 요청할 수 없습니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "참여 또는 대기중인 그룹이 아닙니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "방장(OWNER)은 그룹을 나갈 수 없습니다. 원하시면 그룹을 삭제해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
