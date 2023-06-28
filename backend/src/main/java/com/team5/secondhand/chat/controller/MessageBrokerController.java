package com.team5.secondhand.chat.controller;

import com.team5.secondhand.chat.domain.ChatBubble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageBrokerController {

    private final SimpMessageSendingOperations messageSendingOperations;

    @MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    public ChatBubble testMessageBroker(ChatBubble chat) {
        log.info("CHAT = {}", chat.toString());
        return chat;
    }
}
