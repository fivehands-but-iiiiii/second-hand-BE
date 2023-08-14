package com.team5.secondhand.chat.chatroom.domain;

import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class Chatroom { // NoSQL 에 저장될 자료 구조
    private String chatroomId;
    private Participants participants;
    private String lastMessage;

    @Builder
    private Chatroom(String chatroomId, Participants participants, String lastMessage) {
        this.chatroomId = chatroomId;
        this.participants = participants;
        this.lastMessage = lastMessage;
    }

    public Chatroom init(ChatroomInfo info) {
        return Chatroom.builder()
                .chatroomId(info.getRoomId())
                .participants(Participants.init(info.getMembers()))
                .lastMessage("")
                .build();
    }

    public boolean updateLastMessage (ChatBubble chatBubble) throws NotChatroomMemberException {
        this.lastMessage = chatBubble.getMessage();
        return participants.getMessage(chatBubble.getReceiver());
    }

    public boolean enter(String memberId) {
        return participants.enter(memberId);
    }

    public boolean exit(String memberId) {
        return participants.exit(memberId);
    }
}
