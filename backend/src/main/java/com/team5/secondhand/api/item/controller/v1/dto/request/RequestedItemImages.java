package com.team5.secondhand.api.item.controller.v1.dto.request;

import com.team5.secondhand.api.item.domain.ItemImage;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestedItemImages {
    private String url;

    public ItemImage toEntity() {
        return ItemImage.builder()
                .url(this.url)
                .build();
    }

    public static RequestedItemImages from(ItemImage itemImage) {
        return RequestedItemImages.builder()
                .url(itemImage.getUrl())
                .build();
    }

    public static RequestedItemImages from(String url) {
        return RequestedItemImages.builder()
                .url(url)
                .build();
    }
}
