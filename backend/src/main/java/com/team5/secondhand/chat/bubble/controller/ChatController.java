package com.team5.secondhand.chat.bubble.controller;

import com.team5.secondhand.chat.bubble.controller.dto.ChatBubbleRequest;
import com.team5.secondhand.chat.bubble.service.ChatBubbleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class ChatController {
    private final ChatBubbleService bubbleService;

    @MessageMapping("/message")
    public void message(ChatBubbleRequest message, SimpMessageHeaderAccessor messageHeaderAccessor) {
        bubbleService.handleMessage(message.toDto());
    }

}
