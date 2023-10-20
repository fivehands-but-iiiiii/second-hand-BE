package com.team5.secondhand.api.item.controller.v1.dto.request;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.ItemCounts;
import com.team5.secondhand.api.item.domain.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPostWithUrl {
    @NotNull
    private String title;
    private String contents;
    @NotNull
    private Long category;
    @NotNull
    private Long region;
    private int price;
    @NotNull
    private List<ItemImage> images;

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .category(category)
                .price(price)
                .status(Status.ON_SALE)
                .count(ItemCounts.createRelated())
                .build();
    }
}
