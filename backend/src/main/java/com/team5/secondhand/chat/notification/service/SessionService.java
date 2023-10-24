package com.team5.secondhand.chat.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SessionService {
    private final RedisTemplate<String, String> redisSessionTemplate;
    private final String MAIN_KEY = "sessionStore";

    @Transactional
    public void saveSession(String sessionId, Long memberId) {
        redisSessionTemplate.opsForSet().add(MAIN_KEY, sessionId);
        redisSessionTemplate.opsForValue().set(generateSessionKey(sessionId), String.valueOf(memberId));
    }

    public String getMemberIdBySessionId(String sessionId) {
        return redisSessionTemplate.opsForValue().get(generateSessionKey(sessionId));
    }

    @Transactional
    public void deleteSession(String sessionId) {
        redisSessionTemplate.opsForSet().remove(MAIN_KEY, sessionId);
        redisSessionTemplate.delete(generateSessionKey(sessionId));
    }

    private String generateSessionKey (String sessionId) {
        return String.format("%s:%s", MAIN_KEY, sessionId);
    }
}
