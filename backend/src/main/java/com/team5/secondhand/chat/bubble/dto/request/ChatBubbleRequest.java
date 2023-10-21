package com.team5.secondhand.chat.bubble.dto.request;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ChatBubbleRequest {

    private String roomId;
    private long sender;
    private long receiver;
    private String message;

    @Builder
    protected ChatBubbleRequest(String roomId, long sender, long receiver, String message) {
        this.roomId = roomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public ChatBubble toDomain() {
        return ChatBubble.builder()
                .chatroomId(roomId)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();
    }
}
