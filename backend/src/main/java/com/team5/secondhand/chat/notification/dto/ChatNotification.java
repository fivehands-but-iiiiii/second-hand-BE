package com.team5.secondhand.chat.notification.dto;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatNotification {
    private final String roomId;
    private final String message;
    private final Integer unread;

    public static ChatNotification of(ChatBubble message, Chatroom chatroom) {
        return new ChatNotification(
                message.getRoomId(),
                message.getMessage(),
                chatroom.getParticipants().getInfo().get(message.getReceiver()).getMessageStock()
        );
    }
}
