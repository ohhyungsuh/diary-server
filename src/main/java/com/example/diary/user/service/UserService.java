package com.example.diary.user.service;

import com.example.diary.user.domain.User;
import com.example.diary.user.dto.LoginDto;
import com.example.diary.user.dto.ProfileDto;
import com.example.diary.user.dto.SignupDto;
import com.example.diary.user.dto.UserDto;
import com.example.diary.user.exception.UserErrorCode;
import com.example.diary.user.exception.UserException;
import com.example.diary.user.repository.UserRepository;
import com.example.diary.user.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
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

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        HttpSession session = request.getSession();

        session.setAttribute(SessionConst.USER_ID.getKey(), user.getId());
        session.setMaxInactiveInterval(SessionConst.USER_ID.getExpiration());

        return modelMapper.map(user, UserDto.class);
    }

    public ProfileDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.INVALID_LOGIN_ID));

        return modelMapper.map(user, ProfileDto.class);
    }

    public void logout(HttpSession session) {
        if(session != null) {
            session.invalidate();
        }
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

}
