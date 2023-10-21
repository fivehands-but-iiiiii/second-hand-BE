package com.team5.secondhand.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void put(String key, Object value, Long expirationTime) {
        if (expirationTime != null) {
            redisTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            try {
                String s = objectMapper.writeValueAsString(object);
                return objectMapper.readValue(s, clazz);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private <T> T createInstanceRecursively(Class<T> clazz, LinkedHashMap<String, Object> map) throws
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true); // Make it accessible if it's private
        T instance = constructor.newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            if (map.containsKey(fieldName)) {
                Object fieldValue = map.get(fieldName);
                field.setAccessible(true);

                if (field.getType().isAssignableFrom(Map.class) && fieldValue instanceof LinkedHashMap) {
                    field.set(instance, createInstanceRecursively(field.getType(), (LinkedHashMap<String, Object>) fieldValue));
                } else {
                    field.set(instance, fieldValue);
                }
            }
        }
        return instance;
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
}
