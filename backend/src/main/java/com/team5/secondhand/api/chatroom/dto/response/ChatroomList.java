package com.team5.secondhand.api.chatroom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatroomList {
    private final int page;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<ChatroomSummary> chatRooms;

    public static ChatroomList of(List<ChatroomSummary> chatRooms, int page, boolean hasNext, boolean hasPrevious) {
        return ChatroomList.builder()
                .chatRooms(chatRooms)
                .page(page)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }
}
