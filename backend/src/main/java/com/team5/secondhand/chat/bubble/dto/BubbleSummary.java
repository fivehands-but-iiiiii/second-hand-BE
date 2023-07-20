package com.team5.secondhand.chat.bubble.dto;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BubbleSummary {
    private String id;
    private String senderId;
    private String contents;
    private Boolean isMine;

    @Builder
    private BubbleSummary(String id, String senderId, String contents, Boolean isMine) {
        this.id = id;
        this.senderId = senderId;
        this.contents = contents;
        this.isMine = isMine;
    }

    public static BubbleSummary from(ChatBubble bubble, MemberDetails loginMember) {
        return BubbleSummary.builder()
                .id(bubble.getId())
                .senderId(bubble.getFrom())
                .contents(bubble.getContents())
                .isMine(bubble.isSender(loginMember))
                .build();
    }
}
