package com.team5.secondhand.api.item.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ItemPost {
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
}
