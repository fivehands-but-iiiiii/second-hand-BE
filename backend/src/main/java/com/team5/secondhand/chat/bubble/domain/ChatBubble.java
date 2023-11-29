package com.team5.secondhand.chat.bubble.domain;

import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@NoArgsConstructor
public class ChatBubble implements Serializable {

    private static AtomicLong basicId;

    private Long id;
    private String chatroomId;
    private Long sender;
    private Long receiver;
    private String message;
    private String createdAt;

    @Builder
    private ChatBubble(Long id, String chatroomId, Long sender, Long receiver, String message, String createdAt) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = createdAt;
    }

    @PostConstruct
    private void init() {
        basicId = new AtomicLong(System.currentTimeMillis());
    }

    private static Long generateId() {
        return basicId.getAndIncrement();
    }

    private String generateCreatedAt(String time) {
        if (time == null) {
            return Instant.now().toString();
        }
        return time;
    }

    public Boolean isSender(String memberId) {
        return this.sender.equals(memberId);
    }

    public void ready() {
        this.id = generateId();
        this.createdAt = generateCreatedAt(createdAt);
    }
}
