package com.example.diary.post.exception;

import com.example.diary.global.exception.CustomException;

public class PostException extends CustomException {
    public PostException(PostErrorCode postErrorCode) {
        super(postErrorCode);
    }
}
