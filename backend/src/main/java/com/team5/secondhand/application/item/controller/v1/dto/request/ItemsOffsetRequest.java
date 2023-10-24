package com.team5.secondhand.application.item.controller.v1.dto.request;

import com.team5.secondhand.application.item.service.dto.ItemListFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class ItemsOffsetRequest {
    @Size(min = 0)
    private final int page;
    private final Long sellerId;
    private final Long regionId;
    private final Boolean isSales;
    private final Long categoryId;

    public ItemListFilter toFilter() {
        return ItemListFilter.of(this.categoryId, this.sellerId, this.isSales, this.regionId);
    }
}
