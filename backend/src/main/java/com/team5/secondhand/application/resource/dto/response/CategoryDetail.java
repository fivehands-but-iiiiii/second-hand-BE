package com.team5.secondhand.application.resource.dto.response;

import com.team5.secondhand.application.resource.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CategoryDetail {
    private final Long id;
    private final String title;
    private final String iconUrl;

    public static CategoryDetail of(Category category) {
        return CategoryDetail.builder()
                .id(category.getId())
                .title(category.getTitle())
                .iconUrl(category.getImgUrl())
                .build();
    }
}
