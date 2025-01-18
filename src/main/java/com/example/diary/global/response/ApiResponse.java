package com.example.diary.global.response;

import com.example.diary.global.exception.CustomException;
import com.example.diary.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
public class ApiResponse<T> {
    private final Status status;

    @JsonInclude(NON_EMPTY)
    private Metadata metadata;

    @JsonInclude(NON_EMPTY)
    private List<T> results;

    // 응답 코드만 반환
    public ApiResponse(HttpStatus httpStatus) {
        this.status = new Status(httpStatus);
    }

    // 응답 코드와 단일 데이터 반환
    public ApiResponse(HttpStatus httpStatus, T result) {
        this.status = new Status(httpStatus);
        this.metadata = new Metadata(1);
        this.results = List.of(result);
    }

    // 응답 코드와 다중 데이터 반환
    public ApiResponse(HttpStatus httpStatus, List<T> results) {
        this.status = new Status(httpStatus);
        this.metadata = new Metadata(results.size());
        this.results = results;
    }

    // 에러 처리
    public ApiResponse(CustomException e) {
        this.status = new Status(e.getErrorCode());
    }

    @Getter
    private class Status {
        private final int code;
        private final String message;

        // 커스텀 에러 코드 처리
        public Status(ErrorCode errorCode) {
            this.code = errorCode.getHttpStatus().value();
            this.message = errorCode.getMessage();
        }

        // 일반 응답 반환(OK, CREATED, ACCEPTED 등)
        public Status(HttpStatus httpStatus) {
            this.code = httpStatus.value();
            this.message = httpStatus.getReasonPhrase();
        }
    }

    // 결과 데이터 개수 + a 처리 가능
    @Getter
    @AllArgsConstructor
    private class Metadata {
        private int resultCount = 0;
    }
}
