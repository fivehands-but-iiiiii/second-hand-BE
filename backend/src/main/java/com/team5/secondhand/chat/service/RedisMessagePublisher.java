package com.team5.secondhand.chat.service;

import com.team5.secondhand.chat.domain.ChatBubble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class RedisMessagePublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String topic, ChatBubble message) {
        log.debug("pub log : " +  message.toString() + "/ topic: " + topic);
        redisTemplate.convertAndSend(topic, message);
    }
}
