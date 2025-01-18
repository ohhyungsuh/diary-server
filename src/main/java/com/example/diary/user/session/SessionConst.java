package com.example.diary.user.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SessionConst {

    // userId를 key로 가지며 유효기간은 30분
    USER_ID("userId", 1800);

    private final String key;
    private final int expiration;
}
