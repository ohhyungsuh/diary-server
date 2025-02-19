package com.example.diary.post.domain;

import com.example.diary.global.entity.BaseTimeEntity;
import com.example.diary.group.domain.Group;
import com.example.diary.post.dto.WritePostDto;
import com.example.diary.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Post(String title, String body, User user, Group group) {
        this.title = title;
        this.body = body;
        this.user = user;
        this.group = group;
    }

    public void update(WritePostDto writePostDto) {
        this.title = writePostDto.getTitle();
        this.body = writePostDto.getBody();
    }
}
