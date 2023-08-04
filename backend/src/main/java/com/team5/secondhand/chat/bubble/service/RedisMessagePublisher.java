package com.team5.secondhand.chat.bubble.service;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.ChatBubbleRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class RedisMessagePublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatLogService chatLogService;

    public void publish(String topic, ChatBubble message) {
        log.debug("pub log : " +  message.toString() + "/ topic: " + topic);
        message.ready();
        chatLogService.saveChatBubble(message);
        redisTemplate.convertAndSend(topic, message);
    }
}
