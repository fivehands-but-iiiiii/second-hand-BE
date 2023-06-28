package com.team5.secondhand.api.item.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemDetailImage {
    private int order;
    private String url;

    @Builder
    private ItemDetailImage(int order, String url) {
        this.order = order;
        this.url = url;
    }

    public static ItemDetailImage create (int order, String url) {
        return ItemDetailImage.builder()
                .order(order)
                .url(url)
                .build();
    }

}
