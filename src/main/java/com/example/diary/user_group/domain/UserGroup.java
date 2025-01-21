package com.example.diary.user_group.domain;

import com.example.diary.global.entity.BaseTimeEntity;
import com.example.diary.group.domain.Group;
import com.example.diary.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    @Column
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public void updateRole() {
        this.role = Role.MANAGER;
    }

    public void demoteRole() {
        this.role = Role.MEMBER;
    }

    public void acceptUser() {
        this.status = Status.JOIN;
    }

    public void expelUser() {
        this.status = Status.DENY;
    }

}
