package com.team5.secondhand.chat.bubble.dto.response;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class BubbleSummary {
    private String id;
    private String senderId;
    private String contents;
    private Boolean isMine;
    private Instant createdAt;

    @Builder
    private BubbleSummary(String id, String senderId, String contents, Boolean isMine, Instant createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.contents = contents;
        this.isMine = isMine;
        this.createdAt = createdAt;
    }

    public static BubbleSummary from(ChatBubble bubble, MemberDetails loginMember) {
        return BubbleSummary.builder()
                .id(bubble.getId())
                .senderId(bubble.getFrom())
                .contents(bubble.getMessage())
                .isMine(bubble.isSender(loginMember))
                .createdAt(Instant.parse(bubble.getCreatedAt()))
                .build();
    }
}
