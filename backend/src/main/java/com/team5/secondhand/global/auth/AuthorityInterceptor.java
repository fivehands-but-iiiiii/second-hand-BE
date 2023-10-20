package com.team5.secondhand.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AuthorityInterceptor implements HandlerInterceptor {
    public static final String[] LOGIN_ESSENTIAL = {"/members/**", "/logout", "/chats", "/chats/**"};
    public static final String[] LOGIN_INESSENTIAL = {"/", "/join", "/git/**", "/login"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        return true;
    }


}
