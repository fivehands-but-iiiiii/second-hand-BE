package com.team5.secondhand.chat.controller;

import com.team5.secondhand.chat.domain.ChatBubble;
import com.team5.secondhand.chat.service.RedisMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
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
