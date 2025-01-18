package com.example.diary.user.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {

    public static Long getUserIdBySession(HttpServletRequest request) {
        // Interceptor 통해 session 유무 판별하므로 따로 예외처리 X
        HttpSession session = request.getSession();

        // Object 반환이므로 Long 타입 캐스팅
        return (Long) session.getAttribute(SessionConst.USER_ID.getKey());
    }
}
