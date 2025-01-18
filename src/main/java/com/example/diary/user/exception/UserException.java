package com.example.diary.user.exception;

import com.example.diary.global.exception.CustomException;

public class UserException extends CustomException {

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode);
    }

}
