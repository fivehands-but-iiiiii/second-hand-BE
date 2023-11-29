package com.team5.secondhand.application.item.controller.v1.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class ItemFilteredSlice {
    @Size(min = 0)
    private final int page;
    private final Long sellerId;
    private final Long regionId;
    private final Boolean isSales;
    private final Long categoryId;
}
