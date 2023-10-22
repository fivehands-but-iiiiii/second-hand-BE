package com.team5.secondhand.application.item.controller.v1.dto.response;

import com.team5.secondhand.application.item.controller.v1.dto.request.RequestedItemImages;
import com.team5.secondhand.application.item.domain.Item;
import com.team5.secondhand.application.item.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class ItemDetail {
    private final Long id;
    private final String title;
    private final String contents;
    private final Long category;
    private final int price;
    private final Status status;
    private final Seller seller;
    private final Boolean isMyItem;
    private final List<RequestedItemImages> images;
    private final Long hits;
    private final Long chatCount;
    private final Long likesCount;
    private final Boolean isLike;
    private final Instant createAt;

    public static ItemDetail of(Item item, Boolean isMyItem, Boolean isLike) {
        List<RequestedItemImages> images = item.getContents().getDetailImageUrl().stream()
                .map(RequestedItemImages::from).collect(Collectors.toList());

        return ItemDetail.builder()
                .id(item.getId())
                .title(item.getTitle())
                .contents(item.getContents().getContents())
                .category(item.getCategory())
                .price(item.getPrice())
                .status(item.getStatus())
                .seller(Seller.of(item.getSeller()))
                .isMyItem(isMyItem)
                .images(images)
                .hits(item.getCount().getHits() + 1)
                .chatCount(item.getCount().getChatCounts())
                .likesCount(item.getCount().getLikeCounts())
                .createAt(item.getCreatedAt())
                .isLike(isLike)
                .build();
    }
}
