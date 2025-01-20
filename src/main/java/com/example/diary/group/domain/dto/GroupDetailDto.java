package com.example.diary.group.domain.dto;

import com.example.diary.user_group.domain.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GroupDetailDto {

    @JsonProperty("group")
    private GroupInfo group;

    @JsonProperty("owner")
    private OwnerInfo owner;

    private Status status;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GroupInfo {
        private Long id;
        private String name;
        private String description;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OwnerInfo {
        private Long id;
        private String nickname;
    }
}