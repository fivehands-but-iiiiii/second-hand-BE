package com.team5.secondhand.api.chatroom.dto;

import com.team5.secondhand.api.item.domain.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatItemInfo {
    private final Long itemId;
    private final String title;
    private final Integer price;
    private final String thumbnailImgUrl;
    private final Status status;
    private final Boolean isDelete;
}
