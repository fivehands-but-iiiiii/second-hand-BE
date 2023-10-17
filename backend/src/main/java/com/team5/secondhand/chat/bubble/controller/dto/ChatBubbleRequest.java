package com.team5.secondhand.chat.bubble.controller.dto;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatBubbleRequest {

    private final String roomId;
    private final long sender;
    private final long receiver;
    private final String message;

    public ChatBubble toDto() {
        return ChatBubble.builder()
                .chatroomId(roomId)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();
    }
}
