package com.example.diary.group.controller;

import com.example.diary.global.response.ApiResponse;
import com.example.diary.group.domain.dto.CreateGroupDto;
import com.example.diary.group.domain.dto.GroupDto;
import com.example.diary.group.service.GroupService;
import com.example.diary.user.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    // 그룹 생성
    @PostMapping
    public ApiResponse<GroupDto> createGroup(@RequestBody @Valid CreateGroupDto createGroupDto, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        GroupDto groupDto = groupService.createGroup(userId, createGroupDto);
        return new ApiResponse<>(HttpStatus.CREATED, groupDto);
    }

    // 그룹 삭제

    // 내 그룹 전체 조회

    // 참여중인 내 그룹 조회

    // 대기중인 내 그룹 조회


    // 그룹 한 개 조회
}
