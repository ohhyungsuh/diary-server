package com.example.diary.group.dto;

import com.example.diary.user_group.domain.GroupRole;
import com.example.diary.user_group.domain.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupDto {

    private Long id;
    private String name;
    private GroupRole groupRole;
    private Status status;

    public GroupDto(Long id, String name, GroupRole groupRole, Status status) {
        this.id = id;
        this.name = name;
        this.groupRole = groupRole;
        this.status = status;
    }
}