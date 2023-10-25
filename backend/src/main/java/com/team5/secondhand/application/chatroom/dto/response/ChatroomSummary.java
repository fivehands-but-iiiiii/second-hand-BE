package com.team5.secondhand.application.chatroom.dto.response;

import com.team5.secondhand.application.chatroom.domian.Chatroom;
import com.team5.secondhand.application.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class ChatroomSummary {
    private final String chatroomId;
    private final ChatMember opponent;
    private final ChatItemSummary item;
    private ChatLog chatLogs;
    private Instant lastUpdate;

    @Builder
    public ChatroomSummary(String chatroomId, ChatMember opponent, ChatItemSummary item, ChatLog chatLogs, Instant lastUpdate) {
        this.chatroomId = chatroomId;
        this.opponent = opponent;
        this.item = item;
        this.chatLogs = chatLogs;
        this.lastUpdate = lastUpdate;
    }

    public static ChatroomSummary of(Chatroom chatroom, Member member) {
        return ChatroomSummary.builder()
                .chatroomId(chatroom.getChatroomId().toString())
                .opponent(ChatMember.of(chatroom.findOpponent(member)))
                .item(ChatItemSummary.of(chatroom.getItem()))
                .lastUpdate(chatroom.getCreatedAt())
                .build();
    }

    public static ChatroomSummary of(Chatroom chatroom) {
        return ChatroomSummary.builder()
                .chatroomId(chatroom.getChatroomId().toString())
                .opponent(ChatMember.of(chatroom.getBuyer()))
                .item(ChatItemSummary.of(chatroom.getItem()))
                .lastUpdate(chatroom.getCreatedAt())
                .build();
    }

    public ChatroomSummary addChatLogs(ChatLog chatLog) {
        this.chatLogs = chatLog;
        this.lastUpdate = chatLog.getUpdateAt();
        return this;
    }
}
