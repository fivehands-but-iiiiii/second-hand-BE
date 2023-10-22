package com.team5.secondhand.application.wishlist.dto.response;

import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@RequiredArgsConstructor
public class WishItem {
    private final Long id;
    private final String title;
    private final String region;
    private final String thumbnailUrl;
    private final Instant createdAt;
    private final Integer price;
    private final Status status;
    private final Long chatCount;
    private final Long likeCount;

    public static WishItem of(Item item) {
        return WishItem.builder()
                .id(item.getId())
                .title(item.getTitle())
                .region(item.getRegion().getDistrict())
                .thumbnailUrl(item.getThumbnailUrl())
                .createdAt(item.getCreatedAt())
                .price(item.getPrice())
                .status(item.getStatus())
                .chatCount(item.getCount().getChatCounts())
                .likeCount(item.getCount().getLikeCounts())
                .build();
    }
}
