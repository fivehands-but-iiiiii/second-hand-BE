package com.team5.secondhand.application.chatroom.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ChatItem {
    private Long itemId;
    @Size(min = 0)
    private int page;
}
