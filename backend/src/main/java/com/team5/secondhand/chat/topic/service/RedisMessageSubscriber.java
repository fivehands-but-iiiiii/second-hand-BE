package com.team5.secondhand.chat.topic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisMessageSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        log.debug("on message : ");
        byte[] channel = message.getChannel();
        //redis 로부터 온 메시지를 역질렬화하여
        String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

        ChatBubble chatBubble;
        try {
            chatBubble = objectMapper.readValue(publishMessage, ChatBubble.class);
            log.debug("sub log : "+ message.toString());
            messagingTemplate.convertAndSend("/sub/" + chatBubble.getChatroomId(), chatBubble);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
