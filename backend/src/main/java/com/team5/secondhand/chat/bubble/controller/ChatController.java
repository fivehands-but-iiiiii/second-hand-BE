package com.team5.secondhand.chat.bubble.controller;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.service.RedisMessagePublisher;
import com.team5.secondhand.chat.bubble.service.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
class ChatController {
    private final RedisMessagePublisher redisMessagePublisher;
    private final ChannelTopic channelTopic;

    @MessageMapping("/message")
    public void message(ChatBubble message) {
        log.debug("pub controller");
        redisMessagePublisher.publish(channelTopic.getTopic(), message);
    }

}
