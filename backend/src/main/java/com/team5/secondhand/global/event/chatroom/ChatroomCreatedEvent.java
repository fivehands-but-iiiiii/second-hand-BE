package com.team5.secondhand.global.event.chatroom;

import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.global.model.BaseEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ChatroomCreatedEvent extends BaseEvent {
    private final ChatroomInfo info;
}
