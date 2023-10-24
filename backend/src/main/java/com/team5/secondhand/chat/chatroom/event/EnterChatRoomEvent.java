package com.team5.secondhand.chat.chatroom.event;

import com.team5.secondhand.global.model.BaseEvent;
import lombok.Getter;

@Getter
public class EnterChatRoomEvent extends BaseEvent {

    private final String chatroomId;
    private final Long memberId;

    public EnterChatRoomEvent(String chatroomId, Long memberId) {
        super();
        this.chatroomId = chatroomId;
        this.memberId = memberId;
    }
}
