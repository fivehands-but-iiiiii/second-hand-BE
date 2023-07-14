package com.team5.secondhand.api.chatroom.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatroomInfo {
    private final String opponentId;
    private final ChatItemInfo item;
    private final String chatroomId;
}
