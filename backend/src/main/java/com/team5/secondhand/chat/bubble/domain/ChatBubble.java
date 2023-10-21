package com.team5.secondhand.chat.bubble.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@ToString
@NoArgsConstructor
public class ChatBubble implements Serializable {

    private Long id;
    private String chatroomId;
    private Long sender;
    private Long receiver;
    private String message;
    private String createdAt;

    @Builder
    protected ChatBubble(Long id, String chatroomId, Long sender, Long receiver, String message, Instant createdAt) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = createdAt!=null ? createdAt.toString() : "";
    }

    public Boolean isSender(long memberId) {
        return this.sender == memberId;
    }
}
