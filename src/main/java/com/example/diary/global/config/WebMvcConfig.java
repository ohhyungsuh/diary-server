package com.example.diary.global.config;

import com.example.diary.user.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/users/signup", "/api/v1/users/login", "/api/v1/users/logout",
                        "/error") // ✅ 예외 추가
                .excludePathPatterns("/**").excludePathPatterns(HttpMethod.OPTIONS.toString()); // ✅ OPTIONS 요청 제외
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 허용 경로
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용 메소드
                .allowedOrigins("http://localhost:5173") // 배포시 변경
                .allowedHeaders("*") // 허용 헤더
                .allowCredentials(true) // 인증 정보 허용
                .maxAge(1800); // preflight 요청 캐시(초)
    }
}
