package com.team5.secondhand.application.item.domain;

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

    public static ItemDetailImage from(String url) {
        return ItemDetailImage.builder()
                .url(url)
                .build();
    }

}
