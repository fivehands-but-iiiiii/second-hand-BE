package com.team5.secondhand.api.item.dto.request;

import com.team5.secondhand.api.item.domain.ItemDetailImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemImage {
    private final int order;
    private final String url;

    public ItemDetailImage toEntity() {
        return ItemDetailImage.builder()
                .order(this.order)
                .url(this.url)
                .build();
    }
}
