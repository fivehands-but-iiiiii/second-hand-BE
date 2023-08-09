package com.team5.secondhand.api.chatroom.dto;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ChatroomInfo {
    private final String roomId;
    private final List<String> members;

    public static ChatroomInfo from(Chatroom chatroom) {
        return new ChatroomInfo(chatroom.getChatroomId().toString(), chatroom.getChatroomMemberIds());
    }
}
