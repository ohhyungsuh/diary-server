package com.example.diary.user.controller;

import com.example.diary.global.response.ApiResponse;
import com.example.diary.user.dto.LoginDto;
import com.example.diary.user.dto.ProfileDto;
import com.example.diary.user.dto.SignupDto;
import com.example.diary.user.dto.UserDto;
import com.example.diary.user.service.UserService;
import com.example.diary.user.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<UserDto> signup(@RequestBody @Valid SignupDto signupDto) {
        UserDto userDto = userService.signup(signupDto);
        return new ApiResponse<>(HttpStatus.CREATED, userDto);
    }

    @PostMapping("/login")
    public ApiResponse<UserDto> login(@RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {
        UserDto userDto = userService.login(loginDto, request);
        return new ApiResponse<>(HttpStatus.OK, userDto);
    }

    @GetMapping("/my-profile")
    public ApiResponse<ProfileDto> getMyProfile(HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        ProfileDto profileDto = userService.getMyProfile(userId);
        return new ApiResponse<>(HttpStatus.OK, profileDto);
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request) {
        userService.logout(request.getSession(false));
        return new ApiResponse<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ApiResponse<?> resign(HttpServletRequest request) {
        Long userId = SessionUtils.getUserIdBySession(request);
        userService.resign(userId);
        return new ApiResponse<>(HttpStatus.OK);
    }
}
