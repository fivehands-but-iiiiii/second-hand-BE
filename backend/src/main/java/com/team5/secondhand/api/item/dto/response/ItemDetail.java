package com.team5.secondhand.api.item.dto.response;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.Status;
import com.team5.secondhand.api.item.dto.request.ItemImage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    private final boolean isMyItem;
    private final List<ItemImage> images;
    private final Long hits;
    private final Long chatCount;
    private final Long likesCount;
    private final boolean isLike;

    public static ItemDetail of(Item item, boolean isMyItem) {
        List<ItemImage> images = item.getContents().getDetailImageUrl().stream()
                .map(ItemImage::of).collect(Collectors.toList());

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
                .hits(item.getCount().getHits())
                .chatCount(item.getCount().getChatCounts())
                .likesCount(item.getCount().getLikeCounts())
                .isLike(true) //TODO: 임시데이터 좋아요 목록 처리 후에 변경하기
                .build();
    }
}
