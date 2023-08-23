package com.team5.secondhand.global.event.chatroom;

import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.global.model.BaseEvent;
import lombok.Getter;

@Getter
public class ChatroomMetaInfoCreateEvent extends BaseEvent {

    private ChatroomInfo info;

    public ChatroomMetaInfoCreateEvent(ChatroomInfo info) {
        this.info = info;
    }
}
