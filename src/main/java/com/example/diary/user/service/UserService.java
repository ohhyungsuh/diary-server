package com.example.diary.user.service;

import com.example.diary.group.domain.Group;
import com.example.diary.group.repository.GroupRepository;
import com.example.diary.post.repository.PostRepository;
import com.example.diary.user.domain.User;
import com.example.diary.user.domain.dto.LoginDto;
import com.example.diary.user.domain.dto.ProfileDto;
import com.example.diary.user.domain.dto.SignupDto;
import com.example.diary.user.domain.dto.UserDto;
import com.example.diary.user.exception.UserErrorCode;
import com.example.diary.user.exception.UserException;
import com.example.diary.user.repository.UserRepository;
import com.example.diary.user.session.SessionConst;
import com.example.diary.user_group.repository.UserGroupRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    @Transactional
    public UserDto signup(SignupDto signupDto) {

        isLoginIdExists(signupDto);
        isNicknameExists(signupDto);
        isEmailExists(signupDto);

        User user = User.builder()
                .loginId(signupDto.getLoginId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .email(signupDto.getEmail())
                .birth(signupDto.getBirth())
                .build();

        userRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }

    public UserDto login(LoginDto loginDto, HttpServletRequest request) {
        User user = userRepository.findByLoginId(loginDto.getLoginId())
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        HttpSession session = request.getSession();

        session.setAttribute(SessionConst.USER_ID.getKey(), user.getId());
        session.setMaxInactiveInterval(SessionConst.USER_ID.getExpiration());

        return modelMapper.map(user, UserDto.class);
    }

    public ProfileDto getMyProfile(Long userId) {
        User user = validateUserId(userId);

        return modelMapper.map(user, ProfileDto.class);
    }

    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    // todo soft delete인 경우?
    @Transactional
    public void resign(Long userId) {
        validateUserId(userId);

        List<Long> groupIds = userGroupRepository.findGroupIdsByUserId(userId);

        // group 관련된 userGroup 모두 삭제
        userGroupRepository.deleteByGroupIds(groupIds);

        // group 관련된 post 모두 삭제
        postRepository.deleteByGroupIds(groupIds);

        groupRepository.deleteByGroupIds(groupIds);
        userRepository.deleteById(userId);
    }

    private void isLoginIdExists(SignupDto signupDto) {
        if (userRepository.existsByLoginId(signupDto.getLoginId())) {
            throw new UserException(UserErrorCode.DUPLICATE_LOGIN_ID);
        }
    }

    private void isNicknameExists(SignupDto signupDto) {
        if (userRepository.existsByNickname(signupDto.getNickname())) {
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private void isEmailExists(SignupDto signupDto) {
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }
    }

    private User validateUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));
    }

}
