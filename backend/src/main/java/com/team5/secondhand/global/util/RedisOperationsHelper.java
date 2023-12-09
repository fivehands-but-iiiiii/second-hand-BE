package com.team5.secondhand.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisOperationsHelper extends RedisOperations {

    public RedisOperationsHelper(RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

    public void put(String key, Object value, Long expirationSeconds) {
        if (expirationSeconds != null) {
            redisTemplate.opsForValue().set(key, value, expirationSeconds, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            try {
                return Optional.of(getT(clazz, object));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    public boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setExpireTime(String key, long expirationTime) {
        redisTemplate.expire(key, expirationTime, TimeUnit.SECONDS);
    }

    public long getExpireTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public <T> List<T> findAll(String generateKey, Class<T> clazz) {
        List<Object> objects = redisTemplate.opsForValue()
                .multiGet(redisTemplate.keys(generateKey));

        List<T> result = new ArrayList<>();
        for (Object object : objects) {
            try {
                result.add(getT(clazz, object));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
