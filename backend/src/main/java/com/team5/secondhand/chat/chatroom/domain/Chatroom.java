package com.team5.secondhand.chat.chatroom.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Chatroom {
    private String chatroomId;
    private Participants participants;
    private String lastMessage;

    @Builder
    private Chatroom(String chatroomId, Participants participants, String lastMessage) {
        this.chatroomId = chatroomId;
        this.participants = participants;
        this.lastMessage = lastMessage;
    }

    public void updateLastMessage (String message) {
        this.lastMessage = message;
    }

    public void enter(String memberId) {
        participants.enter(memberId);
    }
}
