package com.team5.secondhand.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public abstract class RedisOperations {

    protected final RedisTemplate<String, Object> redisTemplate;
    protected final ObjectMapper objectMapper;

    protected  <T> T getT(Class<T> clazz, Object object) throws JsonProcessingException {
        String s = objectMapper.writeValueAsString(object);
        return objectMapper.readValue(s, clazz);
    }

    public void deleteAll() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushAll();
            return null;
        });
    }
}
