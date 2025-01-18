package com.example.diary.global.exception;

import lombok.Getter;

// 도메인별로 추상화 클래스를 상속하게 함으로써 커스텀 에러처리 가능
@Getter
public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
