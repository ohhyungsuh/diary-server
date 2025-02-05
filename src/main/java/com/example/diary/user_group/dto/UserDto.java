package com.example.diary.user_group.dto;

import com.example.diary.user_group.domain.GroupRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String nickname;
    private GroupRole groupRole;
    private LocalDateTime updatedAt;

    public UserDto(Long id, String nickname, GroupRole groupRole, LocalDateTime updatedAt) {
        this.id = id;
        this.nickname = nickname;
        this.groupRole = groupRole;
        this.updatedAt = updatedAt;
    }
}
