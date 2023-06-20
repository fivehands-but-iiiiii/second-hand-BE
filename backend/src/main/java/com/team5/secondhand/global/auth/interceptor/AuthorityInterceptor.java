package com.team5.secondhand.global.auth.interceptor;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.global.jwt.util.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorityInterceptor implements HandlerInterceptor {
    public static final String[] LOGIN_ESSENTIAL = {"/members/**", "/logout"};
    public static final String[] LOGIN_INESSENTIAL = {"/", "/join", "/git/**", "/login"};


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberDetails loginMember = (MemberDetails) request.getAttribute("loginMember");
        if (loginMember == null) {
            throw new AuthenticationException("로그인이 필요한 기능입니다.");
        }
        return true;
    }


}
