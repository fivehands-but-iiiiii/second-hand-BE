package com.team5.secondhand.chat.bubble.dto.request;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;

public class ChatbubbleRequest {

    private String chatroomId;
    private Long sender;
    private Long receiver;
    private String message;

    public ChatBubble toDomain() {
        return ChatBubble.builder()
                .chatroomId(chatroomId)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();
    }
}
