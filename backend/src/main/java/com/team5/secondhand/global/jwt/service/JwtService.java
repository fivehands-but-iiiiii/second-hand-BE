package com.team5.secondhand.global.jwt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.application.member.dto.response.MemberDetails;
import com.team5.secondhand.global.properties.JwtProperties;
import com.team5.secondhand.global.jwt.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;

    public String setTokenHeader(MemberDetails member, HttpServletResponse response) throws IOException {
        String token = jwtProperties.getTokenType() + " " + jwtUtils.generateToken(member);

        response.setHeader(jwtProperties.getAuthorizationHeader(), token);
        return token;
    }

    public Optional<String> getMemberId(String authorization) {
        try {
            String token = getToken(authorization);
            Claims claim = jwtUtils.getClaim(token);
            MemberDetails loginMember = objectMapper.readValue((String) claim.get(jwtProperties.getClaimKey()), MemberDetails.class);
            
            return Optional.of(loginMember.getMemberId());
        } catch (JsonProcessingException e) {
            log.error("stomp JsonProcessingException");
        } catch (JwtException e) {
            log.error("stomp JwtException");
        }
        return Optional.empty();
    }

    private String getToken(String authorization) {
        return authorization.split(" ")[1];
    }
}
