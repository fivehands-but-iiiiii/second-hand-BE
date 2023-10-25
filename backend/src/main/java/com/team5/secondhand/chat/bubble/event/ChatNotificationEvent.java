package com.team5.secondhand.chat.bubble.event;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import com.team5.secondhand.global.model.BaseEvent;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatNotificationEvent extends BaseEvent {
    private final Chatroom chatroom;
    private final ChatBubble chatBubble;

    @Builder
    protected ChatNotificationEvent(Chatroom chatroom, ChatBubble chatBubble) {
        this.chatroom = chatroom;
        this.chatBubble = chatBubble;
    }

    public static ChatNotificationEvent of(Chatroom chatroom, ChatBubble chatBubble) {
        return new ChatNotificationEvent(chatroom, chatBubble);
    }
}
