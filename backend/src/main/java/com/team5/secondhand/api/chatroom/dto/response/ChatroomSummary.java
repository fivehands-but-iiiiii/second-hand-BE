package com.team5.secondhand.api.chatroom.dto.response;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import com.team5.secondhand.api.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatroomSummary {
    private final String chatroomId;
    private final ChatMember opponent;
    private final ChatItemSummary item;

    //TODO: 채팅의 마지막 로그 추가

    public static ChatroomSummary of(Chatroom chatroom, Member member) {
        return ChatroomSummary.builder()
                .chatroomId(chatroom.getChatroomId().toString())
                .opponent(ChatMember.of(chatroom.findOpponent(member)))
                .item(ChatItemSummary.of(chatroom.getItem()))
                .build();
    }

    public static ChatroomSummary of(Chatroom chatroom) {
        return ChatroomSummary.builder()
                .chatroomId(chatroom.getChatroomId().toString())
                .opponent(ChatMember.of(chatroom.getBuyer()))
                .item(ChatItemSummary.of(chatroom.getItem()))
                .build();
    }
}
