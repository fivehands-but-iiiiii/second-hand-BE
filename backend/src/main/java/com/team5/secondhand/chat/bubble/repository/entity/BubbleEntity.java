package com.team5.secondhand.chat.bubble.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "chatbubble")
public class BubbleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatroomId;
    private Long sender;
    private Long receiver;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Instant createdAt;

    @Builder
    protected BubbleEntity(Long id, String chatroomId, Long sender, Long receiver, String message, Instant createdAt) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Boolean isSender(long memberId) {
        return this.sender == memberId;
    }

    public static BubbleEntity from(ChatBubble bubble) {
        return BubbleEntity.builder()
                .id(bubble.getId())
                .chatroomId(bubble.getChatroomId())
                .sender(bubble.getSender())
                .receiver(bubble.getReceiver())
                .message(bubble.getMessage())
                .createdAt(Instant.parse(bubble.getCreatedAt()))
                .build();
    }

    public ChatBubble toDomain() {
        return ChatBubble.builder()
                .id(this.id)
                .chatroomId(this.chatroomId)
                .sender(this.sender)
                .receiver(this.receiver)
                .message(this.message)
                .createdAt(this.createdAt.toString())
                .build();
    }

}
