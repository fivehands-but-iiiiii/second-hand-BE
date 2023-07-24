package com.team5.secondhand.chat.bubble.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.api.member.dto.response.MemberDetails;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@RedisHash("chat-bubble")
public class ChatBubble implements Serializable {
    @Id @JsonIgnore
    private final String id;
    @Indexed
    private final String roomId;
    private final String from;
    private final String contents;
    private final String createdAt;

    @Builder
    private ChatBubble(String id, String roomId, String from, String message, String createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.from = from;
        this.contents = message;
        this.createdAt = createdAt;
    }

    @JsonCreator
    public static ChatBubble create(String roomId, String sender, String message) {
        return ChatBubble.builder()
                .id(generateKey())
                .roomId(roomId)
                .from(sender)
                .message(message)
                .createdAt(Instant.now().toString())
                .build();
    }

    private static String generateKey() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public Boolean isSender(MemberDetails loginMember) {
        return this.equals(loginMember.getMemberId());
    }
}