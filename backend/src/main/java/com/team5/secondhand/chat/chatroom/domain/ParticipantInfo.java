package com.team5.secondhand.chat.chatroom.domain;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class ParticipantInfo implements Serializable {
    private String memberId;
    private Instant lastDisconnectedAt;
    private Boolean isConnected;
    private Integer messageStock;

    @Builder
    private ParticipantInfo(String memberId, Instant lastDisconnectedAt, Boolean isConnected, Integer messageStock) {
        this.memberId = memberId;
        this.lastDisconnectedAt = lastDisconnectedAt;
        this.isConnected = isConnected;
        this.messageStock = messageStock;
    }

    public static ParticipantInfo init(String memberId) {
        return ParticipantInfo.builder()
                .memberId(memberId)
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
        this.messageStock = 0;
    }

    public void disconnect() {
        this.isConnected = false;
        this.messageStock = 0;
        this.lastDisconnectedAt = Instant.now();
    }
}
