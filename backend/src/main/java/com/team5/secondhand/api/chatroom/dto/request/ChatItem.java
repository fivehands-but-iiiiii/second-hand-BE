package com.team5.secondhand.api.chatroom.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ChatItem {
    private Long itemId;
    @Size(min = 0)
    private int page;
}
