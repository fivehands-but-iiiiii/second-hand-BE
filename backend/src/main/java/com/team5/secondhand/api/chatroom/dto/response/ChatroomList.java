package com.team5.secondhand.api.chatroom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ChatroomList {
    private final int page;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private List<ChatroomSummary> chatRooms = new ArrayList<>();

    @Builder
    private ChatroomList(int page, boolean hasPrevious, boolean hasNext, List<ChatroomSummary> chatRooms) {
        this.page = page;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.chatRooms = chatRooms;
    }

    public static ChatroomList of(List<ChatroomSummary> chatRooms, int page, boolean hasNext, boolean hasPrevious) {
        return ChatroomList.builder()
                .chatRooms(chatRooms)
                .page(page)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }

    public ChatroomList addLastMessage(List<ChatroomSummary> chatRooms) {
        this.chatRooms = chatRooms;
        return this;
    }

    public void sort() {
        chatRooms.sort(Comparator.comparing(ChatroomSummary::getLastUpdate).reversed());
    }
}
