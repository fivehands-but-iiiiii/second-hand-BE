package com.team5.secondhand.application.chatroom.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class ChatItem {
    private final Long itemId;
    @Size(min = 0)
    private final int page;
}
