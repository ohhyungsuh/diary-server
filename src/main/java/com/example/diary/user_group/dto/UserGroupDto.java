package com.example.diary.user_group.dto;

import com.example.diary.user_group.domain.GroupRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;

@Getter
@NoArgsConstructor
public class UserGroupDto {

    private Long id;
    private String name;
    private GroupRole groupRole;

}
