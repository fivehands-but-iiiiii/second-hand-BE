package com.team5.secondhand.chat.notification.dto;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatNotification {
    private final String roomId;
    private final String message;

    public static ChatNotification of(ChatBubble message) {
        return new ChatNotification(message.getRoomId(), message.getMessage());
    }
}
