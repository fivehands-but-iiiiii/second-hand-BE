package com.team5.secondhand.api.item.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemImage {
    private final int order;
    private final String url;
}
