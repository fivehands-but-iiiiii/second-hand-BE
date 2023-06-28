package com.team5.secondhand.api.wishlist.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class WishItemList {
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<WishItem> items;

    public static WishItemList of(List<WishItem> items, boolean hasNext, boolean hasPrevious) {
        return WishItemList.builder()
                .items(items)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }
}
