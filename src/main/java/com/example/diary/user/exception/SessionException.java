package com.example.diary.user.exception;

import com.example.diary.global.exception.CustomException;

public class SessionException extends CustomException {
    public SessionException(UserErrorCode userErrorCode) {
        super(userErrorCode);
    }
}
