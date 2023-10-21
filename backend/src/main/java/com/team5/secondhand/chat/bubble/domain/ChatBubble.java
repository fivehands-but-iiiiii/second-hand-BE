package com.team5.secondhand.chat.bubble.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "chatbubble")
public class ChatBubble implements Serializable {

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
    protected ChatBubble(Long id, String chatroomId, Long sender, Long receiver, String message, Instant createdAt) {
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
}
