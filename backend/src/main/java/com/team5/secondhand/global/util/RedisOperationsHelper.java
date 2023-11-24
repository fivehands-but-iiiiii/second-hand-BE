package com.team5.secondhand.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisOperationsHelper {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;


    public void put(String key, Object value, Long expirationSeconds) {
        if (expirationSeconds != null) {
            redisTemplate.opsForValue().set(key, value, expirationSeconds, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public void putToList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deleteAll() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushAll();
            return null;
        });
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

    private <T> T getT(Class<T> clazz, Object object) throws JsonProcessingException {
        String s = objectMapper.writeValueAsString(object);
        return objectMapper.readValue(s, clazz);
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

    public <T> List<T> getAll(String generateKey, Class<T> clazz) {
        List<Object> range = redisTemplate.opsForList().range(generateKey, 0, -1);
        return range.stream().map(e -> {
            try {
                return getT(clazz, e);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());
    }

    public <T> List<T> getList(String key, long start, long end, Class<T> clazz) {
        List<Object> range = redisTemplate.opsForList().range(key, start, end-1);

        return range.stream().map(e -> {
            try {
                return getT(clazz, e);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());
    }

    public long size(String key) {
        return redisTemplate.opsForList().size(key);
    }
}
