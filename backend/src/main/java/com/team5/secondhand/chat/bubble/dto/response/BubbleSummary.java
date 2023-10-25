package com.team5.secondhand.chat.bubble.dto.response;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BubbleSummary {
    private String id;
    private Long sender;
    private String message;
    private Boolean isMine;
    private String createdAt;

    @Builder
    private BubbleSummary(String id, Long sender, String contents, Boolean isMine, String createdAt) {
        this.id = id;
        this.sender = sender;
        this.message = contents;
        this.isMine = isMine;
        this.createdAt = createdAt;
    }

    public static BubbleSummary from(ChatBubble bubble, String memberId) {
        return BubbleSummary.builder()
                .id(bubble.getId().toString())
                .sender(bubble.getSender())
                .contents(bubble.getMessage())
                .isMine(bubble.isSender(memberId))
                .createdAt(bubble.getCreatedAt())
                .build();
    }
}
