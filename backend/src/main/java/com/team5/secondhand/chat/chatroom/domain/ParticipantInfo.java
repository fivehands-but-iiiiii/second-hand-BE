package com.team5.secondhand.chat.chatroom.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ParticipantInfo implements Serializable {
    private Long memberId;
    private Instant lastDisconnectedAt;
    private Boolean isConnected;
    private Integer messageStock;

    @Builder
    private ParticipantInfo(Long memberId, Instant lastDisconnectedAt, Boolean isConnected, Integer messageStock) {
        this.memberId = memberId;
        this.lastDisconnectedAt = lastDisconnectedAt;
        this.isConnected = isConnected;
        this.messageStock = messageStock;
    }

    public static ParticipantInfo init(Long memberId) {
        return ParticipantInfo.builder()
                .memberId(memberId)
                .lastDisconnectedAt(null)
                .isConnected(false)
                .messageStock(0)
                .build();
    }

    public static ParticipantInfo of(Long memberId, Boolean isConnected, Integer messageStock) {
        return ParticipantInfo.builder()
                .memberId(memberId)
                .lastDisconnectedAt(null)
                .isConnected(isConnected)
                .messageStock(messageStock)
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
