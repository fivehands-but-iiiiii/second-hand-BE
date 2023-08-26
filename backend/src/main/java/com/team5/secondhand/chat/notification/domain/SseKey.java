package com.team5.secondhand.chat.notification.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class SseKey {
    private final String key;
    private final String memberId;
    private final Instant createdAt;

    @Builder
    private SseKey(String key, String memberId, Instant createdAt) {
        this.key = key;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public static SseKey of(String memberId) {
        Instant createdAt = Instant.now();
        return SseKey.builder()
                .key(generateId(memberId, createdAt))
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
