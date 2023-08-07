package com.team5.secondhand.chat.chatroom.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ParticipantInfo {
    private String memberId;
    private String sessionId;
    private Instant lastDisconnectedAt;
    private Boolean isConnected;
    private Integer messageStock;

    @Builder
    private ParticipantInfo(String memberId, String sessionId, Instant lastDisconnectedAt, Boolean isConnected, Integer messageStock) {
        this.memberId = memberId;
        this.sessionId = sessionId;
        this.lastDisconnectedAt = lastDisconnectedAt;
        this.isConnected = isConnected;
        this.messageStock = messageStock;
    }

    public static ParticipantInfo init(String memberId, String sessionId) {
        return ParticipantInfo.builder()
                .memberId(memberId)
                .sessionId(sessionId)
                .lastDisconnectedAt(null)
                .isConnected(null)
                .messageStock(0)
                .build();
    }

    public void plusBubble() {
        this.messageStock ++;
    }

    public void connect() {
        this.isConnected = true;
    }

    public void disconnect() {
        this.isConnected = false;
        this.lastDisconnectedAt = Instant.now();
    }
}
