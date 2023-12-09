package com.team5.secondhand.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisListOperationsHelper extends RedisOperations {

    public RedisListOperationsHelper(RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

    /**
     * Redis List에 데이터를 추가합니다.
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * Redis List에 데이터를 가져옵니다. (RPOP)
     * @param key
     * @param T
     * @return List<T>
     */
    public <T> List<T> popAll(String key, Class<T> clazz) {
        Set<String> keys = redisTemplate.keys(key+"*");
        List<T> result = new ArrayList<>();

        for (String k : keys) {
            Long size = redisTemplate.opsForList().size(k);
            List<Object> objects = redisTemplate.opsForList().rightPop(k, size);
            result.addAll(objects.stream().map(e -> {
                try {
                    return getT(clazz, e);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }).collect(Collectors.toList()));
        }
        return result;
    }

    /**
     * Redis List 개수를 확인합니다.
     * @param key
     * @return size (long)
     */
    public long size(String key) {
        return redisTemplate.opsForList().size(key);
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

    /**
     * Redis List에서 데이터를 삭제합니다.
     * @param key
     * @param value
     */
    public void trim(String key, int size) {
        redisTemplate.opsForList().trim(key, 0, size);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
