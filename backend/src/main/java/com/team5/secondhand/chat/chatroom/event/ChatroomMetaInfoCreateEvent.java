package com.team5.secondhand.chat.chatroom.event;

import com.team5.secondhand.application.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.global.model.BaseEvent;
import lombok.Getter;

@Getter
public class ChatroomMetaInfoCreateEvent extends BaseEvent {

    private ChatroomInfo info;

    public ChatroomMetaInfoCreateEvent(ChatroomInfo info) {
        this.info = info;
    }
}
