package com.team5.secondhand.api.chatroom.dto.response;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ChatLog {
    private final String lastMessage;
    private final String updateAt;
    private final Integer unReadCount;

    @Builder
    private ChatLog(String lastMessage, String updateAt, Integer unReadCount) {
        this.lastMessage = lastMessage;
        this.updateAt = updateAt;
        this.unReadCount = unReadCount;
    }

    public static ChatLog of(Chatroom chatroom, Long memberId) {
        return ChatLog.builder()
                .lastMessage(chatroom.getLastMessage())
                .updateAt(chatroom.getUpdateAt())
                .unReadCount(chatroom.getParticipants().getInfo().get(memberId).getMessageStock())
                .build();
    }

    public static ChatLog empty() {
        return ChatLog.builder()
                .lastMessage("")
                .updateAt(Instant.now().toString())
                .unReadCount(0)
                .build();
    }
}
