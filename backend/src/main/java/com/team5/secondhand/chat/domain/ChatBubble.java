package com.team5.secondhand.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class ChatBubble {
    private final String roomId;
    private final String from;
    private final String message;

    @Builder
    private ChatBubble(String roomId, String from, String message) {
        this.roomId = roomId;
        this.from = from;
        this.message = message;
    }

    public static ChatBubble create(String roomId, String sender, String message) {
        return ChatBubble.builder()
                .roomId(roomId)
                .from(sender)
                .message(message)
                .build();
    }
}
