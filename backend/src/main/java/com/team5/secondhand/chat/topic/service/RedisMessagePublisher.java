package com.team5.secondhand.chat.topic.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.event.ChatBubbleArrivedEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class RedisMessagePublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void publish(String topic, ChatBubble message) {
        message.ready(); // id, createdAt 설정
        redisTemplate.convertAndSend(topic, message);

        publisher.publishEvent(new ChatBubbleArrivedEvent(message));
    }
}
