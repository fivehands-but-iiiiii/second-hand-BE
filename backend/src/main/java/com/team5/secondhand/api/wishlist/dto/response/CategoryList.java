package com.team5.secondhand.api.wishlist.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class CategoryList {
    public final List<Long> categories;

    public static CategoryList of(List<Long> categories) {
        return CategoryList.builder()
                .categories(categories)
                .build();
    }
}
