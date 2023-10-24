package com.team5.secondhand.chat.chatroom.event;

import com.team5.secondhand.application.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.global.model.BaseEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ChatroomCreatedEvent extends BaseEvent {
    private final ChatroomInfo info;

    public ChatroomCreatedEvent(ChatroomInfo info) {
        this.info = info;
        log.info("chatroom created event = {}", info);
    }
}
