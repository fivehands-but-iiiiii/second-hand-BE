package com.team5.secondhand.chat.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    private ChatRoom(String roomId, String name, Set<WebSocketSession> sessions) {
        this.roomId = roomId;
        this.name = name;
        this.sessions = sessions;
    }

    public static ChatRoom create(String name) {
        return ChatRoom.builder()
                .roomId(generateId())
                .name(name)
                .build();
    }

    public void bindSession(WebSocketSession socketSession) {
        sessions.add(socketSession);
    }

    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}
