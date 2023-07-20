package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
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
    private final ChatBubbleRepository chatBubbleRepository;

    public void publish(String topic, ChatBubble message) {
        log.debug("pub log : " +  message.toString() + "/ topic: " + topic);
        chatBubbleRepository.save(message);
        redisTemplate.convertAndSend(topic, message);
    }
}
