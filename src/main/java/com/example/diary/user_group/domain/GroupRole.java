package com.example.diary.user_group.domain;

import lombok.Getter;

@Getter
public enum GroupRole {
    // 그룹 수정, 삭제, 인원 관리(수락, 거절, 추방)
    OWNER,

    // 인원 관리(수락, 거절, 추방)
    MANAGER,

    // 일반
    MEMBER;
}