package com.example.diary.group.exception;

import com.example.diary.global.exception.CustomException;

public class GroupException extends CustomException {

    public GroupException(GroupErrorCode groupErrorCode) {
        super(groupErrorCode);
    }
}
