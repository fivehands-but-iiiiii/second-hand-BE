package com.team5.secondhand.api.item.dto.request;

import com.team5.secondhand.api.item.domain.ItemDetailImage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ItemImage {
    private final String url;

    public ItemDetailImage toEntity() {
        return ItemDetailImage.builder()
                .url(this.url)
                .build();
    }

    public static ItemImage of(ItemDetailImage i) {
        return ItemImage.builder()
                .url(i.getUrl())
                .build();
    }
}
