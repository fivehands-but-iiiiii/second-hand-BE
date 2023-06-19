package com.team5.secondhand.global.jwt.service;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.global.jwt.util.JwtProperties;
import com.team5.secondhand.global.jwt.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;

    public void setTokenHeader(MemberDetails member, HttpServletResponse response) {
        String token = jwtUtils.generateToken(member);

        response.setHeader(jwtProperties.getAuthorizationHeader(), jwtProperties.getTokenType() + " " + token);
    }

}
