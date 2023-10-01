package com.team5.secondhand.api.item.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage {
    private String url;

    @Builder
    protected ItemImage(String url) {
        this.url = url;
    }

    public static ItemImage from(String url) {
        return new ItemImage(url);
    }
}
