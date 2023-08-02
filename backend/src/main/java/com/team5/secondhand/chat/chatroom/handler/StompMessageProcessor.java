package com.team5.secondhand.chat.chatroom.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class StompMessageProcessor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.debug("stomp command : {} , destination :  {} , sessionId : {}", headerAccessor.getCommand(), headerAccessor.getDestination(), headerAccessor.getSessionId());

        return message;
    }
}
