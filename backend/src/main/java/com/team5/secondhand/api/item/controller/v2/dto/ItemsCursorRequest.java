package com.team5.secondhand.api.item.controller.v2.dto;

import com.team5.secondhand.api.item.service.dto.ItemListFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class ItemsCursorRequest implements Serializable {
    private final Long last;
    private final Long sellerId;
    private final Long regionId;
    private final Boolean isSales;
    private final Long categoryId;

    public ItemListFilter toFilter() {
        return ItemListFilter.of(categoryId, sellerId, isSales, regionId);
    }
}
