package com.team5.secondhand.api.item.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemDetailImage {
    private String url;

    @Builder
    private ItemDetailImage(String url) {
        this.url = url;
    }

    public static ItemDetailImage create (String url) {
        return ItemDetailImage.builder()
                .url(url)
                .build();
    }
}
