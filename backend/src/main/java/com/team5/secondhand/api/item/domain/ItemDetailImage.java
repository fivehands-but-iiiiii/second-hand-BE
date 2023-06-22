package com.team5.secondhand.api.item.domain;

import com.team5.secondhand.api.item.dto.request.ItemImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemDetailImage {
    private int order;
    private String url;

    @Builder
    public ItemDetailImage(int order, String url) {
        this.order = order;
        this.url = url;
    }

}
