package com.team5.secondhand.chat.bubble.domain;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@RedisHash("chat-bubble")
@NoArgsConstructor
public class ChatBubble implements Serializable, Comparable {
    @Id
    private String id;
    @Indexed
    private String roomId;
    private String sender;
    private String receiver;
    private String message;
    private String createdAt;

    @Builder
    private ChatBubble(String id, String roomId, String sender, String receiver, String message, String createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = createdAt;
    }

    private static String generateKey(String id) {
        if (id==null) {
            return UUID.randomUUID().toString();
        }
        return id;
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

    @Override
    public int compareTo(Object o) {
        ChatBubble bubble = (ChatBubble) o;
        if (Instant.parse(this.createdAt).isBefore(Instant.parse(bubble.createdAt))) {
            return 1;
        }
        return -1;
    }

    public void ready() {
        this.id = generateKey(id);
        this.createdAt = generateCreatedAt(createdAt);
    }
}
