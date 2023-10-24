package com.team5.secondhand.chat.notification.dto;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatNotification {
    private String roomId;
    private String message;
    private Integer unread;


    private ChatNotification(String roomId, String message, Integer unread) {
        this.roomId = roomId;
        this.message = message;
        this.unread = unread;
    }

    public static ChatNotification of(ChatBubble message, Chatroom chatroom) {
        return new ChatNotification(
                message.getChatroomId(),
                message.getMessage(),
                chatroom.getParticipants().getInfo().get(message.getReceiver()).getMessageStock()
        );
    }
}
