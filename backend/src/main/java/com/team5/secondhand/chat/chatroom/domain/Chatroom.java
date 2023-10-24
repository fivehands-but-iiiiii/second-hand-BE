package com.team5.secondhand.chat.chatroom.domain;

import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Chatroom implements Serializable { // NoSQL 에 저장될 자료 구조

    private Long id;
    private String chatroomId;
    private Participants participants = new Participants(new ConcurrentHashMap<>());
    private String lastMessage;
    private String updateAt;

    @Builder
    public Chatroom(Long id, String chatroomId, Participants participants, String lastMessage, String updateAt) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.updateAt = updateAt;
    }

    public static Chatroom init(ChatroomInfo info) {
        return Chatroom.builder()
                .chatroomId(info.getRoomId())
                .participants(Participants.init(info.getMembers()))
                .updateAt(Instant.now().toString())
                .lastMessage("")
                .build();
    }

    public static Chatroom create(String chatroomId, Long memberId) {
        return Chatroom.builder()
                .chatroomId(chatroomId)
                .participants(Participants.init(List.of(memberId)))
                .updateAt(Instant.now().toString())
                .lastMessage("")
                .build();
    }

    public boolean updateLastMessage (ChatBubble chatBubble) {
        this.lastMessage = chatBubble.getMessage();
        this.updateAt = Instant.now().toString();
        return participants.getMessage(chatBubble.getReceiver());
    }

    public boolean enter(Long memberId) {
        return participants.enter(memberId);
    }

    public boolean exit(Long memberId) {
        return participants.exit(memberId);
    }

    public boolean hasPaticipant(Long id) {
        return this.participants.hasMember(id);
    }
}
