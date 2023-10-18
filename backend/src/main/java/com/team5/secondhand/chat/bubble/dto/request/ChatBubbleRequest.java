package com.team5.secondhand.chat.bubble.dto.request;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatBubbleRequest {

    private final String roomId;
    private final long sender;
    private final long receiver;
    private final String message;

    public ChatBubble toDomain() {
        return ChatBubble.builder()
                .chatroomId(roomId)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();
    }
}