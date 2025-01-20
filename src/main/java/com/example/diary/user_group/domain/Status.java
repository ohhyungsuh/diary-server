package com.example.diary.user_group.domain;

import lombok.Getter;

@Getter
public enum Status {
    JOIN,
    PENDING,
    NOT_JOIN,
    DENY;
}
