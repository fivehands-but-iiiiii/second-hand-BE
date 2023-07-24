package com.team5.secondhand.api.item.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class MyItemFilteredSlice {
    @Size(min = 0)
    private final int page;
    private final Boolean isSales;
}
