package com.team5.secondhand.api.chatroom.dto.response;

import com.team5.secondhand.api.item.domain.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ChatItemSummary {
    private final Long itemId;
    private final String title;
    private final String thumbnailImgUrl;

    public static ChatItemSummary of(Item item) {
        return ChatItemSummary.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .thumbnailImgUrl(item.getThumbnailUrl())
                .build();
    }
}
