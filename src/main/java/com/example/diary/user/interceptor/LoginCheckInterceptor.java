package com.example.diary.user.interceptor;

import com.example.diary.global.exception.CustomException;
import com.example.diary.user.exception.SessionException;
import com.example.diary.user.exception.UserErrorCode;
import com.example.diary.user.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("요청한 URI: {}", requestURI);

        // 요청으로부터 세션 조회
        HttpSession session = request.getSession(false);

        if(session == null) {
            log.info("세션이 존재하지 않습니다. 로그인 후 이용해주세요.");
            throw new SessionException(UserErrorCode.NOT_FOUND_SESSION);
        }

        /*
         * Map<JSESSIONID, Map<K, V>> 형태로 저장된다.
         * 따라서 K로 가져온 attribute가 null이면 K가 올바르지 않거나(그럴 일이 있을까?), 만료된 것
         */
        if(session.getAttribute(SessionConst.USER_ID.getKey()) == null) {
            log.info("세션이 유효하지 않습니다.");
            throw new SessionException(UserErrorCode.INVALID_SESSION);
        }

        return true;
    }
}
