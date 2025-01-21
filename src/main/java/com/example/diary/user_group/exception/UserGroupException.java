package com.example.diary.user_group.exception;

import com.example.diary.global.exception.CustomException;

public class UserGroupException extends CustomException {

    public UserGroupException(UserGroupErrorCode userGroupErrorCode) {
        super(userGroupErrorCode);
    }
}
