package com.example.diary.user.controller;

import com.example.diary.global.response.ApiResponse;
import com.example.diary.user.dto.LoginDto;
import com.example.diary.user.dto.SignupDto;
import com.example.diary.user.dto.UserDto;
import com.example.diary.user.service.UserService;
import com.example.diary.user.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
