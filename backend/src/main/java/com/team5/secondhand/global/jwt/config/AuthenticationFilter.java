package com.team5.secondhand.global.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.global.jwt.util.JwtProperties;
import com.team5.secondhand.global.jwt.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String headValue = httpServletRequest.getHeader(jwtProperties.getAuthorizationHeader());

        if (headValue != null && headValue.startsWith(jwtProperties.getTokenType())) {
            String token = getToken(headValue);
            Claims claim = jwtUtils.getClaim(token);
            MemberDetails loginMember = objectMapper.readValue((String) claim.get(jwtProperties.getClaimKey()), MemberDetails.class);
            request.setAttribute("loginMember", loginMember);
        }

        chain.doFilter(request, response);
    }

    private String getToken(String headValue) {
        return headValue.split(" ")[1];
    }
}
