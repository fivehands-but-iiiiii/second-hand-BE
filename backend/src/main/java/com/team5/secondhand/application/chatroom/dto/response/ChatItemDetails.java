package com.team5.secondhand.application.chatroom.dto.response;

import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatItemDetails {
    private final Long itemId;
    private final String title;
    private final Integer price;
    private final String thumbnailImgUrl;
    private final Status status;
    private final Boolean isDelete;

    @Builder
    private ChatItemDetails(Long itemId, String title, Integer price, String thumbnailImgUrl, Status status, Boolean isDelete) {
        this.itemId = itemId;
        this.title = title;
        this.price = price;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.status = status;
        this.isDelete = isDelete;
    }

    public static ChatItemDetails from(Item item) {
        return ChatItemDetails.builder().itemId(item.getId())
                .title(item.getTitle())
                .price(item.getPrice())
                .thumbnailImgUrl(item.getThumbnailUrl())
                .status(item.getStatus())
                .isDelete(item.getIsDeleted())
                .build();
    }
}
