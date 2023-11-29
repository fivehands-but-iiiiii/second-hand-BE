package com.team5.secondhand.chat.bubble.controller;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.dto.request.ChatbubbleRequest;
import com.team5.secondhand.chat.topic.service.RedisMessagePublisher;
import com.team5.secondhand.chat.notification.service.SendChatNotificationUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class ChatController {
    private final RedisMessagePublisher redisMessagePublisher;
    private final ChannelTopic chatTopic;

    @MessageMapping("/message")
    public void message(ChatbubbleRequest message, SimpMessageHeaderAccessor messageHeaderAccessor) {
        messageHeaderAccessor.getSessionAttributes();
        log.debug("pub controller");
        redisMessagePublisher.publish(chatTopic.getTopic(), message.toDomain());
    }

}
