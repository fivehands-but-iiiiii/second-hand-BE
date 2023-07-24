package com.team5.secondhand.api.chatroom.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatItem {
    private final Long itemId;
    private final Integer page;
}
