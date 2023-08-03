package com.team5.secondhand.chat.bubble.dto.response;

import com.team5.secondhand.api.member.dto.response.MemberDetails;
import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChatroomLog {
    private final Boolean hasPre;
    private final Boolean hasNext;
    private final List<BubbleSummary> chatBubbles;

    @Builder
    public ChatroomLog(Boolean hasPre, Boolean hasNext, List<BubbleSummary> chatBubbles) {
        this.hasPre = hasPre;
        this.hasNext = hasNext;
        this.chatBubbles = chatBubbles;
    }

    public static ChatroomLog from(Slice<ChatBubble> chatBubbles, MemberDetails loginMember) {
        List<BubbleSummary> bubbleSummaries = chatBubbles.getContent().stream()
                .map(e -> BubbleSummary.from(e, loginMember))
                .collect(Collectors.toList());

        return ChatroomLog.builder()
                .hasPre(chatBubbles.hasPrevious())
                .hasNext(chatBubbles.hasNext())
                .chatBubbles(bubbleSummaries)
                .build();
    }
}
