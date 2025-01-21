package com.example.diary.group.controller;

import com.example.diary.global.response.ApiResponse;
import com.example.diary.group.domain.dto.CreateGroupDto;
import com.example.diary.group.domain.dto.GroupDetailDto;
import com.example.diary.group.domain.dto.GroupDto;
import com.example.diary.group.service.GroupService;
import com.example.diary.user.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    // 그룹 조회
    @GetMapping
    public ApiResponse<GroupDto> getGroups(HttpServletRequest request) {
        SessionUtils.getUserIdBySession(request);
        List<GroupDto> groups = groupService.getGroups();
        return new ApiResponse<>(HttpStatus.OK, groups);
    }

    // 내 그룹 전체 조회
    @GetMapping("/my-group")
    public ApiResponse<GroupDto> getMyGroups(HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        List<GroupDto> myGroups = groupService.getMyGroups(userId);
        return new ApiResponse<>(HttpStatus.OK, myGroups);
    }


    // 그룹 상세 조회
    @GetMapping("/{groupId}")
    public ApiResponse<GroupDetailDto> getGroupDetail(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        GroupDetailDto groupDetailDto = groupService.getGroupDetail(userId, groupId);
        return new ApiResponse<>(HttpStatus.OK, groupDetailDto);
    }

    // 그룹 삭제
    @DeleteMapping("/{groupId}")
    public ApiResponse<?> deleteGroup(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        SessionUtils.getUserIdBySession(request);
        groupService.deleteGroup(groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }
}
