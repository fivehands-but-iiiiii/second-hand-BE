package com.team5.secondhand.chat.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// session 으로 변경
@Service
@RequiredArgsConstructor
public class SessionService {
    //TODO session key pk로 변경
    private final RedisTemplate<String, String> redisSessionTemplate;
    private final String MAIN_KEY = "sessionStore";

    @Transactional
    public void saveSession(String sessionId, String memberId) {
        redisSessionTemplate.opsForSet().add(MAIN_KEY, sessionId);
        redisSessionTemplate.opsForValue().set(generateSessionKey(sessionId), memberId);
    }

    public Long getMemberIdBySessionId(String sessionId) {
        return Long.parseLong(redisSessionTemplate.opsForValue().get(generateSessionKey(sessionId)));
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
