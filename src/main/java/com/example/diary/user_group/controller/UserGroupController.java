package com.example.diary.user_group.controller;

import com.example.diary.global.response.ApiResponse;
import com.example.diary.user.session.SessionUtils;
import com.example.diary.user_group.domain.dto.UserDto;
import com.example.diary.user_group.domain.dto.UserGroupDto;
import com.example.diary.user_group.service.UserGroupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-group")
public class UserGroupController {

    private final UserGroupService userGroupService;

    // todo 알림 테이블 만들어서 실시간 알람 기능 만들어보기
    // 그룹 가입 요청
    @PostMapping("/{groupId}/join")
    public ApiResponse<UserGroupDto> joinGroup(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        UserGroupDto userGroupDto = userGroupService.joinGroup(userId, groupId);
        return new ApiResponse<>(HttpStatus.CREATED, userGroupDto);
    }

    // 그룹 가입 요청 삭제
    @DeleteMapping("/{groupId}/delete")
    public ApiResponse<?> deleteJoinGroup(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        userGroupService.deleteJoinGroup(userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // 그룹 내 인원 조회
    @GetMapping("/{groupId}/users")
    public ApiResponse<UserDto> getUsersInGroup(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        SessionUtils.getUserIdBySession(request);
        List<UserDto> users = userGroupService.getUsersInGroup(groupId);
        return new ApiResponse<>(HttpStatus.OK, users);
    }

    // 그룹 나가기
    @DeleteMapping("/{groupId}/leave")
    public ApiResponse<?> leaveGroup(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        userGroupService.leaveGroup(userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // 가입 요청 들어온 사용자 조회

    // 그룹 가입 요청 수락

    // 그룹 가입 요청 거절

    // 그룹 내 인원 추방

    // 그룹 내 인원 매니저 승진

    // 그룹 내 인원 일반 강등
}
