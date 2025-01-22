package com.example.diary.group.domain;

import com.example.diary.global.entity.BaseTimeEntity;
import com.example.diary.group.dto.CreateGroupDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "groups")
public class Group extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Builder
    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
