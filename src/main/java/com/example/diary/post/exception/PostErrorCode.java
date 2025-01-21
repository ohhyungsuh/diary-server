package com.example.diary.post.exception;

import com.example.diary.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    UNAUTHORIZED_ROLE(HttpStatus.BAD_REQUEST, "작업을 수행할 권한이 없습니다."),
    INVALID_POST_ID(HttpStatus.BAD_REQUEST, "요청하신 게시글이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
