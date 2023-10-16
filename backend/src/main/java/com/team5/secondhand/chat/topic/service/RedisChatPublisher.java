package com.team5.secondhand.chat.topic.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class RedisChatPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Transactional
    public void publish(ChatBubble message) {
        log.debug("pub log : " +  message.toString() + "/ topic: " + topic.getTopic());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
