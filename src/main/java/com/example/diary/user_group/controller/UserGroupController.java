package com.example.diary.user_group.controller;

import com.example.diary.global.response.ApiResponse;
import com.example.diary.user.session.SessionUtils;
import com.example.diary.user_group.dto.UserDto;
import com.example.diary.user_group.dto.UserGroupDto;
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
    // 수정 필요
    @GetMapping("/{groupId}/users")
    public ApiResponse<UserDto> getUsersInGroup(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        List<UserDto> users = userGroupService.getUsersInGroup(userId, groupId);
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
    @GetMapping("/{groupId}/users/join")
    public ApiResponse<UserDto> lookUpJoinUsers(@PathVariable("groupId") Long groupId, HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        List<UserDto> users = userGroupService.lookUpJoinUsers(userId, groupId);
        return new ApiResponse<>(HttpStatus.OK, users);
    }

    // 그룹 가입 요청 수락
    // 업데이트 안 됨
    @PutMapping("/{groupId}/users/join/{userId}/accept")
    public ApiResponse<?> acceptJoinUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId, HttpServletRequest request) {
        Long adminId = SessionUtils.getUserIdBySession(request);
        userGroupService.acceptJoinUser(adminId, userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // 그룹 가입 요청 거절
    @DeleteMapping("/{groupId}/users/join/{userId}/reject")
    public ApiResponse<?> rejectJoinUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId, HttpServletRequest request) {
        Long adminId = SessionUtils.getUserIdBySession(request);
        userGroupService.rejectJoinUser(adminId, userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // 그룹 내 인원 추방
    @PutMapping("/{groupId}/users/{userId}/expel")
    public ApiResponse<?> expelUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId, HttpServletRequest request) {
        Long adminId = SessionUtils.getUserIdBySession(request);
        userGroupService.expelUser(adminId, userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // 그룹 내 인원 매니저 승진
    @PutMapping("/{groupId}/users/{userId}/update")
    public ApiResponse<?> updateUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId, HttpServletRequest request) {
        Long adminId = SessionUtils.getUserIdBySession(request);
        userGroupService.updateUser(adminId, userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

    // 그룹 내 인원 일반 강등
    @PutMapping("/{groupId}/users/{userId}/demote")
    public ApiResponse<?> demoteUser(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId, HttpServletRequest request) {
        Long adminId = SessionUtils.getUserIdBySession(request);
        userGroupService.demoteUser(adminId, userId, groupId);
        return new ApiResponse<>(HttpStatus.OK);
    }

}
