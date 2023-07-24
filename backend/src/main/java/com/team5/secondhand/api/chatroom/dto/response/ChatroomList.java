package com.team5.secondhand.api.chatroom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ChatroomList {
    private final List<ChatroomSummary> chatRooms;

    public static ChatroomList of(List<ChatroomSummary> chatRooms) {
        return ChatroomList.builder()
                .chatRooms(chatRooms)
                .build();
    }
}
