package com.team5.secondhand.api.resource.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class CategoryList {
    private final List<CategoryDetail> categories;

    public static CategoryList of(List<CategoryDetail> categories) {
        return CategoryList.builder()
                .categories(categories)
                .build();
    }
}
