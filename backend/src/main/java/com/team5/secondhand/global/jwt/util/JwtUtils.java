package com.team5.secondhand.global.jwt.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtProperties jwtProperties;
    public String generateToken(Object object) {
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

    public Claims getClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
        }
        throw new JwtException("잘못된 토큰입니당");
    }
}

