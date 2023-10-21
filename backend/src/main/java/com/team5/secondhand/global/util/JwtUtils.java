package com.team5.secondhand.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.global.properties.JwtProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    public String generateToken(Object object) throws IOException {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .claim(jwtProperties.getClaimKey(), objectMapper.writeValueAsString(object))
                .setExpiration(expiryDate)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //TODO: refresh token 생성

    public Claims getClaim(String token) throws JwtException {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
            throw new JwtException("토큰이 유효하지 않습니다");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
            throw new JwtException("지원하지 않는 토큰 타입입니다");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            throw new JwtException("빈 토큰입니다");
        }
    }
}
