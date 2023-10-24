package com.team5.secondhand.application.item.controller.v1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class CategoryList {
    private final List<Long> id;

    public static CategoryList of(List<Long> category) {
        return CategoryList.builder()
                .id(category)
                .build();
    }
}
