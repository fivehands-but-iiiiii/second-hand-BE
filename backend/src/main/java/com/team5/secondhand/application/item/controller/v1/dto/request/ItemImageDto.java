package com.team5.secondhand.application.item.controller.v1.dto.request;

import com.team5.secondhand.application.item.domain.ItemImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImageDto {
    private String url;

    @Builder
    private ItemImageDto(String url) {
        this.url = url;
    }

    public ItemImage toEntity() {
        return ItemImage.builder()
                .url(this.url)
                .build();
    }

    public static ItemImageDto of(ItemImage i) {
        return ItemImageDto.builder()
                .url(i.getUrl())
                .build();
    }
}
