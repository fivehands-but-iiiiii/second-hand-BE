package com.team5.secondhand.api.item.controller.v1.dto.request;

import com.team5.secondhand.api.item.domain.ItemDetailImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemImage {
    private String url;

    @Builder
    private ItemImage(String url) {
        this.url = url;
    }

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
