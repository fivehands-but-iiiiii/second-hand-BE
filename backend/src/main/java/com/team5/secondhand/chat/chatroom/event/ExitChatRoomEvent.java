package com.team5.secondhand.chat.chatroom.event;

import com.team5.secondhand.global.model.BaseEvent;
import lombok.Getter;

@Getter
public class ExitChatRoomEvent extends BaseEvent {

    private final String chatroomId;
    private final Long memberId;

    public ExitChatRoomEvent(String roomId, Long memberId) {
        super();
        this.chatroomId = roomId;
        this.memberId = memberId;
    }
}
