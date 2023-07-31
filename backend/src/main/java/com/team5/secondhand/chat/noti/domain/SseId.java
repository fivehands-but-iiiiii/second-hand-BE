package com.team5.secondhand.chat.noti.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class SseId {
    private final String id;
    private final String memberId;
    private final Instant createdAt;

    @Builder
    private SseId(String id, String memberId, Instant createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public static SseId of(String memberId) {
        Instant createdAt = Instant.now();
        return SseId.builder()
                .id(generateId(memberId, createdAt))
                .memberId(memberId)
                .createdAt(createdAt)
                .build();
    }

    private static String generateId(String memberId, Instant createdAt) {
        return String.format("%s_%d",memberId,createdAt.getEpochSecond());
    }

    public boolean startsWith(String memberId) {
        return this.memberId.equals(memberId);
    }
}
