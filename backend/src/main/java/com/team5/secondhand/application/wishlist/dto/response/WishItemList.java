package com.team5.secondhand.application.wishlist.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class WishItemList {
    private final int page;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<WishItem> items;

    public static WishItemList of(List<WishItem> items, int page, boolean hasNext, boolean hasPrevious) {
        return WishItemList.builder()
                .page(page)
                .items(items)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }
}
