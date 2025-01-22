package com.example.diary.user_group.domain;

import com.example.diary.global.entity.BaseTimeEntity;
import com.example.diary.group.domain.Group;
import com.example.diary.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_group")
public class UserGroup extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column
    @Enumerated(EnumType.STRING)
    private GroupRole groupRole;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public UserGroup(User user, Group group, GroupRole groupRole, Status status) {
        this.user = user;
        this.group = group;

        this.groupRole = groupRole != null ? groupRole : GroupRole.MEMBER;
        this.status = status != null ? status : Status.PENDING;
    }


    public void updateRole() {
        this.groupRole = GroupRole.MANAGER;
    }

    public void demoteRole() {
        this.groupRole = GroupRole.MEMBER;
    }

    public void acceptUser() {
        this.status = Status.JOIN;
    }

    public void expelUser() {
        this.status = Status.DENY;
    }

}
