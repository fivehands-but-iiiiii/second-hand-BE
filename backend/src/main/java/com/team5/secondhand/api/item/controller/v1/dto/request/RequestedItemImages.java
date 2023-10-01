package com.team5.secondhand.api.item.controller.v1.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestedItemImages {
    private String url;

    @Builder
    private RequestedItemImages(String url) {
        this.url = url;
    }

    public com.team5.secondhand.api.item.domain.ItemImage toEntity() {
        return com.team5.secondhand.api.item.domain.ItemImage.builder()
                .url(this.url)
                .build();
    }

    public static RequestedItemImages of(com.team5.secondhand.api.item.domain.ItemImage i) {
        return RequestedItemImages.builder()
                .url(i.getUrl())
                .build();
    }
}
