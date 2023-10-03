package com.team5.secondhand.api.item.controller.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.item.domain.ItemContents;
import com.team5.secondhand.api.item.domain.ItemCounts;
import com.team5.secondhand.api.item.domain.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ItemUpdateRequest {
    @NotNull
    private final String title;
    private final String contents;
    @NotNull
    private final Long category;
    @NotNull
    private final Long region;
    private final int price;
    @NotNull
    private final List<RequestedItemImages> images;

    @JsonIgnore
    public Optional<RequestedItemImages> getFirstImageUrl() {
        return images.stream().sorted().findAny();
    }

    public Item toEntity() {
        return Item.builder()
                .title(title)
                .category(category)
                .price(price)
                .status(Status.ON_SALE)
                .contents(ItemContents.of(contents, images.stream().map(RequestedItemImages::toEntity).collect(Collectors.toList())))
                .count(ItemCounts.initCounts())
                .build();
    }
}
