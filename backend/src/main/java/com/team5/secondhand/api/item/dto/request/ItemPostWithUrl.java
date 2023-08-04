package com.team5.secondhand.api.item.dto.request;

import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.ItemContents;
import com.team5.secondhand.api.item.domain.ItemCounts;
import com.team5.secondhand.api.item.domain.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class ItemPostWithUrl {
    @NotNull
    private final String title;
    private final String contents;
    @NotNull
    private final Long category;
    @NotNull
    private final Long region;
    private final int price;
    @NotNull
    private final List<ItemImage> images;

    public Optional<ItemImage> getFirstImageUrl() {
        return images.stream().sorted().findAny();
    }

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
