package com.example.diary.global.exception;

import com.example.diary.global.response.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// CustomException에서 처리된 RuntimeException을 핸들러 처리하여 예외 발생시 API 자동 응답
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleCustomException(CustomException e) {
        return new ApiResponse<>(e);
    }

}
